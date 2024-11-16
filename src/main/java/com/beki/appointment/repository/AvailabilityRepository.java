package com.beki.appointment.repository;

import com.beki.appointment.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByProviderIdAndIsBookedFalse(Long providerId);
}
