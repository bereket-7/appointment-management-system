package com.beki.appointment.repository;

import com.beki.appointment.common.AppointmentStatus;
import com.beki.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByProviderId(Long providerId);

    List<Appointment> findByTargetDate(Date targetDate);


    @Query("SELECT COUNT(DISTINCT a.provider.id) FROM Appointment a")
    long countDistinctProviders();

    long countByAppointmentStatus(AppointmentStatus appointmentStatus);


}
