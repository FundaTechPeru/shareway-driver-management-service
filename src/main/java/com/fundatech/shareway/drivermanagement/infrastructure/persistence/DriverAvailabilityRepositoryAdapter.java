package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.Comparator;
import java.util.List;

import com.fundatech.shareway.drivermanagement.domain.model.DriverAvailability;
import com.fundatech.shareway.drivermanagement.domain.repository.DriverAvailabilityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DriverAvailabilityRepositoryAdapter implements DriverAvailabilityRepository {

    private static final Comparator<DriverAvailability> WEEKLY_ORDER = Comparator
            .comparing(DriverAvailability::getDayOfWeek)
            .thenComparing(DriverAvailability::getStartTime);

    private final SpringDataDriverAvailabilityRepository jpaRepository;

    public DriverAvailabilityRepositoryAdapter(SpringDataDriverAvailabilityRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DriverAvailability> findAllByDriverId(Long driverId) {
        return jpaRepository.findAllByDriverId(driverId).stream()
                .sorted(WEEKLY_ORDER)
                .toList();
    }

    @Override
    public void deleteAllByDriverId(Long driverId) {
        jpaRepository.deleteAllByDriverId(driverId);
    }

    @Override
    public List<DriverAvailability> saveAll(List<DriverAvailability> slots) {
        return jpaRepository.saveAll(slots).stream().sorted(WEEKLY_ORDER).toList();
    }
}
