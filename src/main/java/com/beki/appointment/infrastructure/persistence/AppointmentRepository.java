package com.beki.appointment.infrastructure.persistence;

import com.beki.appointment.domain.appointment.Appointment;
import com.beki.appointment.domain.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByUserId(Long userId);
    
    List<Appointment> findByProviderId(Long providerId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    
    List<Appointment> findByUserIdAndAppointmentDateAfter(Long userId, LocalDateTime dateTime);
    
    List<Appointment> findByUserIdAndAppointmentDateBefore(Long userId, LocalDateTime dateTime);
    
    List<Appointment> findByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(DISTINCT a.providerId) FROM Appointment a")
    long countDistinctProviders();

    long countByStatus(AppointmentStatus status);
}
