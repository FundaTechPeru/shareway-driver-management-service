package com.fundatech.shareway.drivermanagement.infrastructure.security;

import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(User.normalizeEmail(username))
                .map(AuthenticatedUser::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Loads the user named by the {@code sub} claim of an access token, which is the user UUID.
     */
    @Transactional(readOnly = true)
    public Optional<UserDetails> loadUserById(UUID id) {
        return userRepository.findById(id).map(AuthenticatedUser::from);
    }
}
