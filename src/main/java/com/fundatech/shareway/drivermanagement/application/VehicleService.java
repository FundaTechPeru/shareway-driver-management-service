package com.fundatech.shareway.drivermanagement.application;

import java.util.List;

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
    public Vehicle register(Long driverId, VehicleCommand command) {
        if (vehicleRepository.existsByPlate(Vehicle.normalizePlate(command.plate()))) {
            throw new ConflictException("Plate is already registered");
        }
        return vehicleRepository.save(Vehicle.register(driverId, command.plate(), command.brand(), command.model(),
                command.year(), command.color(), command.seats()));
    }

    @Transactional(readOnly = true)
    public List<Vehicle> listForDriver(Long driverId) {
        return vehicleRepository.findAllByDriverId(driverId);
    }

    @Transactional(readOnly = true)
    public Vehicle getForDriver(Long driverId, Long vehicleId) {
        return vehicleRepository.findByIdAndDriverId(vehicleId, driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Transactional
    public Vehicle update(Long driverId, Long vehicleId, VehicleCommand command) {
        Vehicle vehicle = getForDriver(driverId, vehicleId);
        if (vehicleRepository.existsByPlateAndIdNot(Vehicle.normalizePlate(command.plate()), vehicleId)) {
            throw new ConflictException("Plate is already registered");
        }
        vehicle.update(command.plate(), command.brand(), command.model(), command.year(), command.color(), command.seats());
        return vehicleRepository.save(vehicle);
    }
}
