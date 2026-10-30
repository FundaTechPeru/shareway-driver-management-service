package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.List;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DriverAvailability;

public interface DriverAvailabilityRepository {

    List<DriverAvailability> findAllByDriverId(UUID driverId);

    void deleteAllByDriverId(UUID driverId);

    List<DriverAvailability> saveAll(List<DriverAvailability> slots);
}
