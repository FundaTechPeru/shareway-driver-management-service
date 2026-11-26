package com.fundatech.shareway.drivermanagement.interfaces.rest;

import java.util.UUID;

import com.fundatech.shareway.drivermanagement.application.VehicleService;
import com.fundatech.shareway.drivermanagement.interfaces.rest.dto.VehicleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lookups that the administration panel needs about a driver. Secured to ADMIN in SecurityConfig.
 */
@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Drivers (admin)", description = "Driver lookups for the administration panel")
public class AdminDriverController {

    private final VehicleService vehicleService;

    public AdminDriverController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}/vehicle")
    @Operation(summary = "Get the current vehicle of a driver",
            description = "Administrators only. Returns the driver's current vehicle, or 404 if the "
                    + "driver has none (US-36).")
    public VehicleResponse vehicle(@PathVariable UUID id) {
        return VehicleResponse.from(vehicleService.getCurrentVehicleForDriver(id));
    }
}
