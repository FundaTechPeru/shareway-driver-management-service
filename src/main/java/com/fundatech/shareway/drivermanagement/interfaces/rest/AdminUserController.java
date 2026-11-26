package com.fundatech.shareway.drivermanagement.interfaces.rest;

import com.fundatech.shareway.drivermanagement.application.UserDirectoryService;
import com.fundatech.shareway.drivermanagement.domain.model.Role;
import com.fundatech.shareway.drivermanagement.domain.repository.UserQuery;
import com.fundatech.shareway.drivermanagement.interfaces.rest.dto.UserResponse;
import com.fundatech.shareway.drivermanagement.shared.PagedResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administrative directory of users for the admin panel "Usuarios" screen.
 * Secured to ADMIN in {@code SecurityConfig} via the {@code /api/v1/users} matcher.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users (admin)", description = "Administrative listing of platform users")
public class AdminUserController {

    private final UserDirectoryService userDirectoryService;

    public AdminUserController(UserDirectoryService userDirectoryService) {
        this.userDirectoryService = userDirectoryService;
    }

    @GetMapping
    @Operation(summary = "List users",
            description = "Administrators only. Optional filters: role (exact), email and fullName (case-insensitive "
                    + "contains). Paginated; size is capped at 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A page of users"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameter (e.g. an unknown role)", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content()),
            @ApiResponse(responseCode = "403", description = "The caller is not an administrator", content = @Content())
    })
    public PagedResult<UserResponse> list(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userDirectoryService.search(new UserQuery(role, email, fullName, page, size))
                .map(UserResponse::from);
    }
}
