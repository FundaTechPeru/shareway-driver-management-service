package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);
}
