package com.fundatech.shareway.drivermanagement.application;

import java.util.List;

import com.fundatech.shareway.drivermanagement.domain.model.DriverAvailability;
import com.fundatech.shareway.drivermanagement.domain.repository.DriverAvailabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvailabilityService {

    private final DriverAvailabilityRepository availabilityRepository;

    public AvailabilityService(DriverAvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Transactional(readOnly = true)
    public List<DriverAvailability> getWeeklySchedule(Long driverId) {
        return availabilityRepository.findAllByDriverId(driverId);
    }

    @Transactional
    public List<DriverAvailability> replaceWeeklySchedule(Long driverId, List<AvailabilitySlotCommand> slots) {
        List<DriverAvailability> schedule = slots.stream()
                .map(slot -> DriverAvailability.create(driverId, slot.dayOfWeek(), slot.startTime(), slot.endTime()))
                .toList();
        DriverAvailability.ensureNoOverlaps(schedule);

        availabilityRepository.deleteAllByDriverId(driverId);
        return availabilityRepository.saveAll(schedule);
    }
}
