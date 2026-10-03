package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataVehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByIdAndDriverId(Long id, Long driverId);

    List<Vehicle> findAllByDriverIdOrderByIdAsc(Long driverId);

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdNot(String plate, Long id);
}
