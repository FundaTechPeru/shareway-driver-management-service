package com.fundatech.shareway.drivermanagement.domain.event;

import java.time.Instant;

import com.fundatech.shareway.drivermanagement.domain.model.DocumentType;

public record DocumentRejected(Long documentId, Long driverId, DocumentType documentType, String reason,
                               Instant occurredAt) implements DomainEvent {

    public DocumentRejected(Long documentId, Long driverId, DocumentType documentType, String reason) {
        this(documentId, driverId, documentType, reason, Instant.now());
    }
}
