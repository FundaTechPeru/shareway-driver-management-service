package com.fundatech.shareway.drivermanagement.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.fundatech.shareway.drivermanagement.application.AccessToken;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private final JwtService jwtService =
            new JwtService(new JwtProperties("unit-test-secret-key-with-at-least-32-bytes", 60));

    @Test
    void issuedTokenCarriesSubjectAndExpiration() {
        AccessToken token = jwtService.issue("ana.torres@upc.edu.pe");

        assertThat(token.expiresInSeconds()).isEqualTo(3600);
        assertThat(jwtService.extractSubject(token.value())).contains("ana.torres@upc.edu.pe");
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() {
        JwtService otherService = new JwtService(new JwtProperties("another-secret-key-with-at-least-32-bytes!", 60));
        String foreignToken = otherService.issue("ana.torres@upc.edu.pe").value();

        assertThat(jwtService.extractSubject(foreignToken)).isEmpty();
    }

    @Test
    void rejectsExpiredAndMalformedTokens() {
        JwtService expiredService = new JwtService(new JwtProperties("unit-test-secret-key-with-at-least-32-bytes", -1));
        String expiredToken = expiredService.issue("ana.torres@upc.edu.pe").value();

        assertThat(jwtService.extractSubject(expiredToken)).isEmpty();
        assertThat(jwtService.extractSubject("not-a-jwt")).isEmpty();
    }
}
