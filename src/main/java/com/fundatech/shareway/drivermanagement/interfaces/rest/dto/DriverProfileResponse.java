package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import com.fundatech.shareway.drivermanagement.domain.model.DriverStatus;
import com.fundatech.shareway.drivermanagement.domain.model.EmergencyContact;
import com.fundatech.shareway.drivermanagement.domain.model.User;

public record DriverProfileResponse(
        Long userId,
        String fullName,
        String email,
        String licenseNumber,
        DriverStatus driverStatus,
        EmergencyContactResponse emergencyContact) {

    public record EmergencyContactResponse(String name, String phone, String relationship) {

        static EmergencyContactResponse from(EmergencyContact contact) {
            return new EmergencyContactResponse(contact.getName(), contact.getPhone(), contact.getRelationship());
        }
    }

    public static DriverProfileResponse from(User user) {
        return new DriverProfileResponse(user.getId(), user.getFullName(), user.getEmail(), user.getLicenseNumber(),
                user.getDriverStatus(), EmergencyContactResponse.from(user.getEmergencyContact()));
    }
}
