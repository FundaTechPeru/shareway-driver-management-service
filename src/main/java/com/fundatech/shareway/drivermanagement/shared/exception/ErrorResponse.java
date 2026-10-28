package com.fundatech.shareway.drivermanagement.shared.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> fieldErrors) {

    public record FieldErrorDetail(String field, String message) {
    }

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return of(status, message, path, List.of());
    }

    public static ErrorResponse of(HttpStatus status, String message, String path, List<FieldErrorDetail> fieldErrors) {
        return new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors);
    }
}
