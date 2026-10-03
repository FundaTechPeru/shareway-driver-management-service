package com.fundatech.shareway.drivermanagement.domain.event;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
