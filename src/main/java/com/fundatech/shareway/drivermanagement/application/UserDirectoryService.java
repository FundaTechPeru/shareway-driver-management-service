package com.fundatech.shareway.drivermanagement.application;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserQuery;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import com.fundatech.shareway.drivermanagement.shared.PagedResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Administrative read model over the user directory. Backs the "Usuarios" admin screen.
 */
@Service
public class UserDirectoryService {

    private final UserRepository userRepository;

    public UserDirectoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PagedResult<User> search(UserQuery query) {
        return userRepository.search(query);
    }
}
