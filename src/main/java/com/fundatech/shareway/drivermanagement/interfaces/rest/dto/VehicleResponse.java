package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import java.time.Instant;

import com.fundatech.shareway.drivermanagement.domain.model.Vehicle;

public record VehicleResponse(
        Long id,
        String plate,
        String brand,
        String model,
        int year,
        String color,
        int seats,
        Instant createdAt,
        Instant updatedAt) {

    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getPlate(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getYear(), vehicle.getColor(), vehicle.getSeats(), vehicle.getCreatedAt(), vehicle.getUpdatedAt());
    }
}
