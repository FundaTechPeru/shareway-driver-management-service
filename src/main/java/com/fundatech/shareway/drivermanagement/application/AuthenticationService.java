package com.fundatech.shareway.drivermanagement.application;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenProvider accessTokenProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, AccessTokenProvider accessTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
    }

    public AccessToken login(String email, String password) {
        String normalizedEmail = User.normalizeEmail(email);
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(normalizedEmail, password));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return accessTokenProvider.issue(normalizedEmail);
    }
}
