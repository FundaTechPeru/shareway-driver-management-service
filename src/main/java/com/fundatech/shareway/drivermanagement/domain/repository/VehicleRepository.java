package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.Vehicle;

public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findByIdAndDriverId(Long id, Long driverId);

    List<Vehicle> findAllByDriverId(Long driverId);

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdNot(String plate, Long id);
}
