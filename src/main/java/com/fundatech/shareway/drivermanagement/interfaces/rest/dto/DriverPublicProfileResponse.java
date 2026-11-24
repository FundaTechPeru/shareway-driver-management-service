package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DriverStatus;
import com.fundatech.shareway.drivermanagement.domain.model.User;

/**
 * Public data of a driver that a passenger may see before the trip (US-07): the name, the
 * verification status (as a proxy for trust) and the license number.
 *
 * <p>TODO: the average rating and the trip history are not owned by this service. They must come
 * from Trip Execution later and be composed by the gateway or the panel; this service does not hold
 * them, so they are deliberately absent here instead of being invented.
 */
public record DriverPublicProfileResponse(
        UUID id,
        String fullName,
        DriverStatus driverStatus,
        String licenseNumber) {

    public static DriverPublicProfileResponse from(User driver) {
        return new DriverPublicProfileResponse(driver.getId(), driver.getFullName(), driver.getDriverStatus(),
                driver.getLicenseNumber());
    }
}
