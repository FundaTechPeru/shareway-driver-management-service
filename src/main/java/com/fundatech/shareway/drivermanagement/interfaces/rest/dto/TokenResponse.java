package com.fundatech.shareway.drivermanagement.interfaces.rest.dto;

import com.fundatech.shareway.drivermanagement.application.AccessToken;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {

    public static TokenResponse from(AccessToken token) {
        return new TokenResponse(token.value(), "Bearer", token.expiresInSeconds());
    }
}
