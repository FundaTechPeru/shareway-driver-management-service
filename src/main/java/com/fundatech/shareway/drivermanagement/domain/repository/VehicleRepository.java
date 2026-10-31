package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.Vehicle;

public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findByIdAndDriverId(UUID id, UUID driverId);

    List<Vehicle> findAllByDriverId(UUID driverId);

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdNot(String plate, UUID id);
}
