package com.fundatech.shareway.drivermanagement.interfaces.rest;

import java.util.UUID;

import com.fundatech.shareway.drivermanagement.application.UserProfileService;
import com.fundatech.shareway.drivermanagement.interfaces.rest.dto.DriverPublicProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Drivers", description = "Public information about drivers")
public class DriverController {

    private final UserProfileService userProfileService;

    public DriverController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{id}/public-profile")
    @Operation(summary = "Get the public profile of a driver",
            description = "Any authenticated user. Returns 404 if the user is not a driver (US-07).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The driver's public profile"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content()),
            @ApiResponse(responseCode = "404", description = "The user is unknown or is not a driver", content = @Content())
    })
    public DriverPublicProfileResponse publicProfile(@PathVariable UUID id) {
        return DriverPublicProfileResponse.from(userProfileService.getPublicDriverProfile(id));
    }
}
