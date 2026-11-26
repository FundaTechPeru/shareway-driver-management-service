package com.fundatech.shareway.drivermanagement.domain.repository;

import com.fundatech.shareway.drivermanagement.domain.model.Role;

/**
 * Optional filters and paging for the administrative user listing.
 * {@code email} and {@code fullName} are matched as case-insensitive "contains".
 */
public record UserQuery(Role role, String email, String fullName, int page, int size) {

    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public UserQuery {
        page = Math.max(page, 0);
        size = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        email = blankToNull(email);
        fullName = blankToNull(fullName);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
