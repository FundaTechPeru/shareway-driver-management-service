package com.fundatech.shareway.drivermanagement.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.application.AccessToken;
import com.fundatech.shareway.drivermanagement.domain.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "unit-test-secret-key-with-at-least-32-bytes";
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String EMAIL = "ana.torres@upc.edu.pe";

    private final JwtService jwtService = new JwtService(new JwtProperties(SECRET, 60));

    @Test
    void issuedTokenCarriesUserIdAndExpiration() {
        AccessToken token = jwtService.issue(USER_ID, EMAIL, Role.DRIVER);

        assertThat(token.expiresInSeconds()).isEqualTo(3600);
        assertThat(jwtService.extractUserId(token.value())).contains(USER_ID);
    }

    @Test
    void issuedTokenCarriesTheClaimsReadByOtherServices() {
        String token = jwtService.issue(USER_ID, EMAIL, Role.DRIVER).value();

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        assertThat(claims.getSubject()).isEqualTo(USER_ID.toString());
        assertThat(claims.get("userId", String.class)).isEqualTo(USER_ID.toString());
        assertThat(claims.get("email", String.class)).isEqualTo(EMAIL);
        assertThat(claims.get("role", String.class)).isEqualTo("DRIVER");
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() {
        JwtService otherService = new JwtService(new JwtProperties("another-secret-key-with-at-least-32-bytes!", 60));
        String foreignToken = otherService.issue(USER_ID, EMAIL, Role.DRIVER).value();

        assertThat(jwtService.extractUserId(foreignToken)).isEmpty();
    }

    @Test
    void rejectsExpiredAndMalformedTokens() {
        JwtService expiredService = new JwtService(new JwtProperties(SECRET, -1));
        String expiredToken = expiredService.issue(USER_ID, EMAIL, Role.DRIVER).value();

        assertThat(jwtService.extractUserId(expiredToken)).isEmpty();
        assertThat(jwtService.extractUserId("not-a-jwt")).isEmpty();
    }

    @Test
    void rejectsTokenWhoseSubjectIsNotAUuid() {
        String legacyToken = Jwts.builder()
                .subject(EMAIL)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThat(jwtService.extractUserId(legacyToken)).isEmpty();
    }
}
