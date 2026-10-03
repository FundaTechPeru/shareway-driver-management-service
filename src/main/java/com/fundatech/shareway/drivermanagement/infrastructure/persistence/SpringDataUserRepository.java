package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
