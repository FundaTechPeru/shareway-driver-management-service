package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.shared.PagedResult;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

    /** Paged administrative search with optional role, email and full-name filters. */
    PagedResult<User> search(UserQuery query);
}
