package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import java.time.Instant;

import com.fundatech.shareway.drivermanagement.domain.model.DocumentStatus;
import com.fundatech.shareway.drivermanagement.domain.model.DocumentType;
import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;

public record DocumentResponse(
        Long id,
        Long driverId,
        DocumentType type,
        DocumentStatus status,
        String originalFilename,
        String contentType,
        long sizeBytes,
        String rejectionReason,
        Instant uploadedAt,
        Instant reviewedAt) {

    public static DocumentResponse from(DriverDocument document) {
        return new DocumentResponse(document.getId(), document.getDriverId(), document.getType(), document.getStatus(),
                document.getOriginalFilename(), document.getContentType(), document.getSizeBytes(),
                document.getRejectionReason(), document.getUploadedAt(), document.getReviewedAt());
    }
}
