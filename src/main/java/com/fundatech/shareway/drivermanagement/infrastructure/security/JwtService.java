package com.fundatech.shareway.drivermanagement.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.fundatech.shareway.drivermanagement.application.AccessToken;
import com.fundatech.shareway.drivermanagement.application.AccessTokenProvider;
import com.fundatech.shareway.drivermanagement.domain.model.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

/**
 * Issues and validates HS256 access tokens. Claims: {@code sub} and {@code userId} are the user UUID,
 * {@code email} the user email and {@code role} one of {@link Role}. The API gateway and the other services
 * read these claims with the same JWT_SECRET.
 */
@Service
public class JwtService implements AccessTokenProvider {

    static final String USER_ID_CLAIM = "userId";
    static final String EMAIL_CLAIM = "email";
    static final String ROLE_CLAIM = "role";

    private final SecretKey signingKey;
    private final Duration expiration;

    public JwtService(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofMinutes(properties.expirationMinutes());
    }

    @Override
    public AccessToken issue(UUID userId, String email, Role role) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(userId.toString())
                .claim(USER_ID_CLAIM, userId.toString())
                .claim(EMAIL_CLAIM, email)
                .claim(ROLE_CLAIM, role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(signingKey)
                .compact();
        return new AccessToken(token, expiration.toSeconds());
    }

    /**
     * Returns the user UUID of a valid token, or empty when the token is invalid, expired or its subject is not a UUID.
     */
    public Optional<UUID> extractUserId(String token) {
        try {
            String subject = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Optional.ofNullable(subject).map(UUID::fromString);
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
