package com.fundatech.shareway.drivermanagement.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import com.fundatech.shareway.drivermanagement.application.AccessToken;
import com.fundatech.shareway.drivermanagement.application.AccessTokenProvider;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements AccessTokenProvider {

    private final SecretKey signingKey;
    private final Duration expiration;

    public JwtService(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofMinutes(properties.expirationMinutes());
    }

    @Override
    public AccessToken issue(String subject) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(signingKey)
                .compact();
        return new AccessToken(token, expiration.toSeconds());
    }

    public Optional<String> extractSubject(String token) {
        try {
            return Optional.ofNullable(Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
