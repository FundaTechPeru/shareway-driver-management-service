package com.fundatech.shareway.drivermanagement.application;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilitySlotCommand(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
}
