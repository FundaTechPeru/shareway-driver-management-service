package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.List;

import com.fundatech.shareway.drivermanagement.domain.model.DriverAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataDriverAvailabilityRepository extends JpaRepository<DriverAvailability, Long> {

    List<DriverAvailability> findAllByDriverId(Long driverId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from DriverAvailability a where a.driverId = :driverId")
    void deleteAllByDriverId(@Param("driverId") Long driverId);
}
