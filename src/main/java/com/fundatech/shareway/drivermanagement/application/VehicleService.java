package com.fundatech.shareway.drivermanagement.application;

import java.util.List;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.Vehicle;
import com.fundatech.shareway.drivermanagement.domain.repository.VehicleRepository;
import com.fundatech.shareway.drivermanagement.shared.exception.ConflictException;
import com.fundatech.shareway.drivermanagement.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle register(UUID driverId, VehicleCommand command) {
        if (vehicleRepository.existsByPlate(Vehicle.normalizePlate(command.plate()))) {
            throw new ConflictException("Plate is already registered");
        }
        return vehicleRepository.save(Vehicle.register(driverId, command.plate(), command.brand(), command.model(),
                command.year(), command.color(), command.seats()));
    }

    @Transactional(readOnly = true)
    public List<Vehicle> listForDriver(UUID driverId) {
        return vehicleRepository.findAllByDriverId(driverId);
    }

    /**
     * The current vehicle of a driver, for the administration panel's trip records (US-36).
     * A driver may register more than one vehicle; the first is enough for now.
     *
     * TODO: this returns the driver's CURRENT vehicle, not necessarily the one used on a past trip.
     * Trip Execution does not store a vehicleId per trip yet, so this is an approximation until that
     * is resolved on the Trip Execution side (not this service's concern).
     */
    @Transactional(readOnly = true)
    public Vehicle getCurrentVehicleForDriver(UUID driverId) {
        return vehicleRepository.findAllByDriverId(driverId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The driver has no registered vehicle"));
    }

    @Transactional(readOnly = true)
    public Vehicle getForDriver(UUID driverId, UUID vehicleId) {
        return vehicleRepository.findByIdAndDriverId(vehicleId, driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Transactional
    public Vehicle update(UUID driverId, UUID vehicleId, VehicleCommand command) {
        Vehicle vehicle = getForDriver(driverId, vehicleId);
        if (vehicleRepository.existsByPlateAndIdNot(Vehicle.normalizePlate(command.plate()), vehicleId)) {
            throw new ConflictException("Plate is already registered");
        }
        vehicle.update(command.plate(), command.brand(), command.model(), command.year(), command.color(), command.seats());
        return vehicleRepository.save(vehicle);
    }
}
