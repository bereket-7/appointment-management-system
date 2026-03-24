package com.beki.appointment.infrastructure.persistence;

import com.beki.appointment.domain.serviceprovider.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByProviderIdAndIsBookedFalse(Long providerId);
}
