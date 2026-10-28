package com.fundatech.shareway.drivermanagement.application;

public record RegisterDriverCommand(
        String licenseNumber,
        String emergencyContactName,
        String emergencyContactPhone,
        String emergencyContactRelationship) {
}
