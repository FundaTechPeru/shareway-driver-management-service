package com.fundatech.shareway.drivermanagement.bdd;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fundatech.shareway.drivermanagement.domain.event.DomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RecordedDomainEvents {

    private final List<DomainEvent> events = new CopyOnWriteArrayList<>();

    @EventListener
    public void record(DomainEvent event) {
        events.add(event);
    }

    public void clear() {
        events.clear();
    }

    public List<String> names() {
        return events.stream().map(event -> event.getClass().getSimpleName()).toList();
    }
}
