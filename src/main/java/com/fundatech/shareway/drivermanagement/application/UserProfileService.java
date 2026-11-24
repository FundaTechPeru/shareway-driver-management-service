package com.fundatech.shareway.drivermanagement.application;

import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import com.fundatech.shareway.drivermanagement.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getProfile(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getDriverProfile(UUID userId) {
        User user = getProfile(userId);
        if (!user.isDriver()) {
            throw new ResourceNotFoundException("Driver profile not found");
        }
        return user;
    }

    /**
     * Public profile of a driver for any authenticated user (US-07). Returns 404 when the user does
     * not exist or is not a driver, so a passenger can only look up real drivers.
     */
    @Transactional(readOnly = true)
    public User getPublicDriverProfile(UUID driverId) {
        User user = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        if (!user.isDriver()) {
            throw new ResourceNotFoundException("Driver not found");
        }
        return user;
    }
}
