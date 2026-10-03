package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import java.time.Instant;

import com.fundatech.shareway.drivermanagement.domain.model.Role;
import com.fundatech.shareway.drivermanagement.domain.model.User;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String phone,
        Role role,
        Instant createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(),
                user.getRole(), user.getCreatedAt());
    }
}
