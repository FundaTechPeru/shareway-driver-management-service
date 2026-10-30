package com.fundatech.shareway.drivermanagement.application;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, AccessTokenProvider accessTokenProvider,
                                 UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.userRepository = userRepository;
    }

    public AccessToken login(String email, String password) {
        String normalizedEmail = User.normalizeEmail(email);
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(normalizedEmail, password));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        return accessTokenProvider.issue(user.getId(), user.getEmail(), user.getRole());
    }
}
