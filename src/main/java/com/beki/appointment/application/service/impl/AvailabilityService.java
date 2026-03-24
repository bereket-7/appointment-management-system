package com.beki.appointment.application.service.impl;

import com.beki.appointment.interfaces.dto.AvailabilityDto;
import com.beki.appointment.domain.serviceprovider.Availability;
import com.beki.appointment.infrastructure.persistence.AvailabilityRepository;
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

    public List<Availability> getAvailableSlots(Long providerId) {
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }

    public List<AvailabilityDto> getAvailabilityByProvider(Long providerId) {
        return null;
    }

    public void deleteAvailability(Long id) {
    }
}

