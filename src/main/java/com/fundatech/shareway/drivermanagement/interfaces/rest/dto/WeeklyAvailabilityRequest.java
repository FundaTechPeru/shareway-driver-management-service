package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import java.util.List;

import com.fundatech.shareway.drivermanagement.application.AvailabilitySlotCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WeeklyAvailabilityRequest(
        @NotNull @Size(max = 50)
        List<@NotNull @Valid AvailabilitySlotRequest> slots) {

    public List<AvailabilitySlotCommand> toCommands() {
        return slots.stream().map(AvailabilitySlotRequest::toCommand).toList();
    }
}
