package com.fundatech.shareway.drivermanagement.shared.exception;

import java.util.Optional;

/**
 * Raised when input passes Bean Validation but violates a domain rule; mapped to HTTP 400.
 */
public class DomainValidationException extends RuntimeException {

    private final String field;

    public DomainValidationException(String message) {
        this(null, message);
    }

    public DomainValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public Optional<String> getField() {
        return Optional.ofNullable(field);
    }
}
