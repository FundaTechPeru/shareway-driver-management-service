package com.fundatech.shareway.drivermanagement.domain.event;

import java.time.Instant;

public record DriverVerified(Long driverId, String email, Instant occurredAt) implements DomainEvent {

    public DriverVerified(Long driverId, String email) {
        this(driverId, email, Instant.now());
    }
}
