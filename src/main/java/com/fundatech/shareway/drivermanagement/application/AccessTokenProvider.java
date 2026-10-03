package com.fundatech.shareway.drivermanagement.application;

public interface AccessTokenProvider {

    AccessToken issue(String subject);
}
