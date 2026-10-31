package com.fundatech.shareway.drivermanagement.domain.event;

import java.time.Instant;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DocumentType;

public record DocumentRejected(UUID documentId, UUID driverId, DocumentType documentType, String reason,
                               Instant occurredAt) implements DomainEvent {

    public DocumentRejected(UUID documentId, UUID driverId, DocumentType documentType, String reason) {
        this(documentId, driverId, documentType, reason, Instant.now());
    }
}
