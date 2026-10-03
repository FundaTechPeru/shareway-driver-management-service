package com.fundatech.shareway.drivermanagement.interfaces.rest;

import com.fundatech.shareway.drivermanagement.application.UserProfileService;
import com.fundatech.shareway.drivermanagement.infrastructure.security.AuthenticatedUser;
import com.fundatech.shareway.drivermanagement.interfaces.rest.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
@Tag(name = "Users", description = "Profile of the authenticated user")
public class UserController {

    private final UserProfileService userProfileService;

    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @Operation(summary = "Get my profile")
    public UserResponse me(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser principal) {
        return UserResponse.from(userProfileService.getProfile(principal.id()));
    }
}
