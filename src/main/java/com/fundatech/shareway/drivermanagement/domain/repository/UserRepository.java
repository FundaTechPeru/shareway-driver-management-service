package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.User;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
