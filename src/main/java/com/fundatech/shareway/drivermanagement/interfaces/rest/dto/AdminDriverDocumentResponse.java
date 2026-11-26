package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import java.time.Instant;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.application.DriverDocumentWithDriver;
import com.fundatech.shareway.drivermanagement.domain.model.DocumentStatus;
import com.fundatech.shareway.drivermanagement.domain.model.DocumentType;
import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import com.fundatech.shareway.drivermanagement.domain.model.User;

/**
 * A driver document as the administration panel reads it: the document plus the driver who submitted
 * it, so the "Verificaciones" screen can show the queue without a second call.
 */
public record AdminDriverDocumentResponse(
        UUID id,
        DocumentType type,
        DocumentStatus status,
        String fileName,
        Instant submittedAt,
        Instant reviewedAt,
        Driver driver) {

    public record Driver(UUID id, String fullName, String email, String phone, String licenseNumber) {

        static Driver from(UUID driverId, User user) {
            if (user == null) {
                return new Driver(driverId, null, null, null, null);
            }
            return new Driver(user.getId(), user.getFullName(), user.getEmail(), user.getPhone(),
                    user.getLicenseNumber());
        }
    }

    public static AdminDriverDocumentResponse from(DriverDocumentWithDriver pair) {
        DriverDocument document = pair.document();
        return new AdminDriverDocumentResponse(
                document.getId(),
                document.getType(),
                document.getStatus(),
                document.getOriginalFilename(),
                document.getUploadedAt(),
                document.getReviewedAt(),
                Driver.from(document.getDriverId(), pair.driver()));
    }
}
