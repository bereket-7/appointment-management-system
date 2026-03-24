package com.beki.appointment.application.service;

import com.beki.appointment.interfaces.dto.AppointmentDto;
import com.beki.appointment.domain.appointment.Appointment;
import com.beki.appointment.domain.appointment.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    
    Appointment createAppointment(AppointmentDto appointmentDto);
    
    Optional<Appointment> getAppointmentById(Long id);
    
    List<Appointment> getAppointmentsByUserId(Long userId);
    
    List<Appointment> getAppointmentsByProviderId(Long providerId);
    
    List<Appointment> getAppointmentsByStatus(AppointmentStatus status);
    
    Appointment updateAppointment(Long id, AppointmentDto appointmentDto);
    
    Appointment cancelAppointment(Long id);
    
    Appointment rescheduleAppointment(Long id, LocalDateTime newDateTime);
    
    Appointment confirmAppointment(Long id);
    
    Appointment completeAppointment(Long id);
    
    List<Appointment> getUpcomingAppointments(Long userId);
    
    List<Appointment> getPastAppointments(Long userId);
    
    List<Appointment> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
