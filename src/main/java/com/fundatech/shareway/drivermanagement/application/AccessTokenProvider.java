package com.fundatech.shareway.drivermanagement.application;

import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.Role;

public interface AccessTokenProvider {

    AccessToken issue(UUID userId, String email, Role role);
}
