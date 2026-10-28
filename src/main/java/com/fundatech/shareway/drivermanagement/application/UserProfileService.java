package com.fundatech.shareway.drivermanagement.application;

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
    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getDriverProfile(Long userId) {
        User user = getProfile(userId);
        if (!user.isDriver()) {
            throw new ResourceNotFoundException("Driver profile not found");
        }
        return user;
    }
}
