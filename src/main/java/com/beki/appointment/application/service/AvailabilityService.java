package com.beki.appointment.application.service;

import com.beki.appointment.interfaces.dto.AvailabilityDto;
import com.beki.appointment.domain.serviceprovider.Availability;

import java.util.List;
import java.util.Optional;

public interface AvailabilityService {
    
    Availability createAvailability(AvailabilityDto availabilityDto);
    
    Optional<Availability> getAvailabilityById(Long id);
    
    List<Availability> getAvailabilityByProviderId(Long providerId);
    
    List<Availability> getAvailableSlots(Long providerId);
    
    Availability updateAvailability(Long id, AvailabilityDto availabilityDto);
    
    void deleteAvailability(Long id);
    
    Availability bookAvailability(Long id);
    
    Availability cancelBooking(Long id);
    
    List<Availability> getAvailabilityByDay(Long providerId, String dayOfWeek);
    
    List<Availability> getAvailabilityByDateRange(Long providerId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    boolean isProviderAvailable(Long providerId, java.time.LocalDateTime dateTime);
    
    List<Availability> getUpcomingAvailability(Long providerId);
}
