package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.Role;
import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserQuery;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import com.fundatech.shareway.drivermanagement.shared.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpaRepository;

    public UserRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLicenseNumber(String licenseNumber) {
        return jpaRepository.existsByLicenseNumber(licenseNumber);
    }

    @Override
    public PagedResult<User> search(UserQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> page = jpaRepository.findAll(Specification.allOf(filters(query)), pageable);
        return new PagedResult<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    private static List<Specification<User>> filters(UserQuery query) {
        List<Specification<User>> specs = new ArrayList<>();
        if (query.role() != null) {
            specs.add(roleEquals(query.role()));
        }
        if (query.email() != null) {
            specs.add(fieldContains("email", query.email()));
        }
        if (query.fullName() != null) {
            specs.add(fieldContains("fullName", query.fullName()));
        }
        return specs;
    }

    private static Specification<User> roleEquals(Role role) {
        return (root, criteria, cb) -> cb.equal(root.get("role"), role);
    }

    private static Specification<User> fieldContains(String field, String value) {
        String pattern = "%" + value.toLowerCase() + "%";
        return (root, criteria, cb) -> cb.like(cb.lower(root.get(field)), pattern);
    }
}
