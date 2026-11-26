package com.fundatech.shareway.drivermanagement.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class JwtSecretValidatorTest {

    private static final String STRONG_SECRET = "a-strong-production-secret-with-enough-length-123";

    @Test
    void failsWhenSecretIsMissingInProduction() {
        assertThatThrownBy(() -> validate("", "prod"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT_SECRET must be set");
    }

    @Test
    void failsWhenSecretIsMissingAndNoProfileIsActive() {
        assertThatThrownBy(() -> validate(""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT_SECRET must be set");
    }

    @Test
    void failsWhenSecretIsStillTheDevelopmentExampleInProduction() {
        assertThatThrownBy(() -> validate(JwtSecretValidator.EXAMPLE_SECRET, "prod"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("example value");
    }

    @Test
    void failsWhenSecretIsTooShortInProduction() {
        assertThatThrownBy(() -> validate("short-secret", "prod"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("too short");
    }

    @Test
    void acceptsAStrongSecretInProduction() {
        assertThatCode(() -> validate(STRONG_SECRET, "prod")).doesNotThrowAnyException();
    }

    @Test
    void acceptsTheExampleSecretUnderTheDevProfile() {
        assertThatCode(() -> validate(JwtSecretValidator.EXAMPLE_SECRET, "dev")).doesNotThrowAnyException();
    }

    @Test
    void acceptsAnyThingUnderTheTestProfile() {
        assertThatCode(() -> validate("test-only-shareway-driver-management-secret-key", "test"))
                .doesNotThrowAnyException();
    }

    private static void validate(String secret, String... activeProfiles) {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles(activeProfiles);
        new JwtSecretValidator(new JwtProperties(secret, 60), environment).validate();
    }
}
