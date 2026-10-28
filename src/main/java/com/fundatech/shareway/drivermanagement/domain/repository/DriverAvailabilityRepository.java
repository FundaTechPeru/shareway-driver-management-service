package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.List;

import com.fundatech.shareway.drivermanagement.domain.model.DriverAvailability;

public interface DriverAvailabilityRepository {

    List<DriverAvailability> findAllByDriverId(Long driverId);

    void deleteAllByDriverId(Long driverId);

    List<DriverAvailability> saveAll(List<DriverAvailability> slots);
}
