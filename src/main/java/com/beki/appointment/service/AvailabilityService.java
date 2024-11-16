package com.beki.appointment.service;

import com.beki.appointment.model.Availability;
import com.beki.appointment.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public Availability createAvailability(Long providerId, LocalDateTime startTime, LocalDateTime endTime) {
        Availability availability = new Availability();
        availability.setProviderId(providerId);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        return availabilityRepository.save(availability);
    }

    public List<Availability> getAvailableSlots(Long providerId) {
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }
}

