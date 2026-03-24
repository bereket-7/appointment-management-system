package com.beki.appointment.application.service.impl;

import com.beki.appointment.application.service.AvailabilityService;
import com.beki.appointment.interfaces.dto.AvailabilityDto;
import com.beki.appointment.domain.serviceprovider.Availability;
import com.beki.appointment.infrastructure.persistence.AvailabilityRepository;
import com.beki.appointment.shared.exception.GeneralException;
import com.beki.appointment.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    @Override
    public Availability createAvailability(AvailabilityDto availabilityDto) {
        log.info("Creating availability for provider {}", availabilityDto.getProviderId());
        
        Availability availability = new Availability();
        availability.setProviderId(availabilityDto.getProviderId());
        availability.setDayOfWeek(availabilityDto.getDayOfWeek());
        availability.setStartTime(availabilityDto.getStartTime());
        availability.setEndTime(availabilityDto.getEndTime());
        availability.setIsBooked(false);

        Availability savedAvailability = availabilityRepository.save(availability);
        log.info("Successfully created availability with ID: {}", savedAvailability.getAvailabilityId());
        return savedAvailability;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Availability> getAvailabilityById(Long id) {
        return availabilityRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getAvailabilityByProviderId(Long providerId) {
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getAvailableSlots(Long providerId) {
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }

    @Override
    public Availability updateAvailability(Long id, AvailabilityDto availabilityDto) {
        log.info("Updating availability with ID: {}", id);
        
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));

        if (availabilityDto.getDayOfWeek() != null) {
            availability.setDayOfWeek(availabilityDto.getDayOfWeek());
        }
        if (availabilityDto.getStartTime() != null) {
            availability.setStartTime(availabilityDto.getStartTime());
        }
        if (availabilityDto.getEndTime() != null) {
            availability.setEndTime(availabilityDto.getEndTime());
        }

        Availability updatedAvailability = availabilityRepository.save(availability);
        log.info("Successfully updated availability with ID: {}", updatedAvailability.getAvailabilityId());
        return updatedAvailability;
    }

    @Override
    public void deleteAvailability(Long id) {
        log.info("Deleting availability with ID: {}", id);
        
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));

        availabilityRepository.delete(availability);
        log.info("Successfully deleted availability with ID: {}", id);
    }

    @Override
    public Availability bookAvailability(Long id) {
        log.info("Booking availability with ID: {}", id);
        
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));

        if (availability.isBooked()) {
            throw new GeneralException("Availability is already booked");
        }

        availability.setIsBooked(true);
        Availability updatedAvailability = availabilityRepository.save(availability);
        log.info("Successfully booked availability with ID: {}", id);
        return updatedAvailability;
    }

    @Override
    public Availability cancelBooking(Long id) {
        log.info("Cancelling booking for availability with ID: {}", id);
        
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));

        availability.setIsBooked(false);
        Availability updatedAvailability = availabilityRepository.save(availability);
        log.info("Successfully cancelled booking for availability with ID: {}", id);
        return updatedAvailability;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getAvailabilityByDay(Long providerId, String dayOfWeek) {
        return availabilityRepository.findByProviderIdAndDayOfWeekAndIsBookedFalse(providerId, dayOfWeek);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getAvailabilityByDateRange(Long providerId, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement date range filtering logic
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProviderAvailable(Long providerId, LocalDateTime dateTime) {
        // TODO: Implement availability checking logic
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId).size() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getUpcomingAvailability(Long providerId) {
        // TODO: Implement upcoming availability logic
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }
}
