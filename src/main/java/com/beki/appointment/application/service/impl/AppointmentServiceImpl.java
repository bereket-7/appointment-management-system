package com.beki.appointment.application.service.impl;

import com.beki.appointment.application.service.AppointmentService;
import com.beki.appointment.interfaces.dto.AppointmentDto;
import com.beki.appointment.domain.appointment.Appointment;
import com.beki.appointment.domain.appointment.AppointmentStatus;
import com.beki.appointment.infrastructure.persistence.AppointmentRepository;
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
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment createAppointment(AppointmentDto appointmentDto) {
        log.info("Creating new appointment for user: {}", appointmentDto.getUserId());
        
        Appointment appointment = new Appointment();
        appointment.setUserId(appointmentDto.getUserId());
        appointment.setProviderId(appointmentDto.getProviderId());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes(appointmentDto.getNotes());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Successfully created appointment with ID: {}", savedAppointment.getAppointmentId());
        return savedAppointment;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByProviderId(Long providerId) {
        return appointmentRepository.findByProviderId(providerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentDto appointmentDto) {
        log.info("Updating appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointmentDto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        }
        if (appointmentDto.getNotes() != null) {
            appointment.setNotes(appointmentDto.getNotes());
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Successfully updated appointment with ID: {}", updatedAppointment.getAppointmentId());
        return updatedAppointment;
    }

    @Override
    public Appointment cancelAppointment(Long id) {
        log.info("Cancelling appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new GeneralException("Appointment is already cancelled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new GeneralException("Cannot cancel completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Successfully cancelled appointment with ID: {}", id);
        return updatedAppointment;
    }

    @Override
    public Appointment rescheduleAppointment(Long id, LocalDateTime newDateTime) {
        log.info("Rescheduling appointment with ID: {} to {}", id, newDateTime);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new GeneralException("Cannot reschedule cancelled appointment");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new GeneralException("Cannot reschedule completed appointment");
        }
        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new GeneralException("Cannot reschedule to past date");
        }

        appointment.setAppointmentDate(newDateTime);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Successfully rescheduled appointment with ID: {}", id);
        return updatedAppointment;
    }

    @Override
    public Appointment confirmAppointment(Long id) {
        log.info("Confirming appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new GeneralException("Cannot confirm appointment that is not scheduled");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Successfully confirmed appointment with ID: {}", id);
        return updatedAppointment;
    }

    @Override
    public Appointment completeAppointment(Long id) {
        log.info("Completing appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new GeneralException("Cannot complete appointment that is not confirmed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Successfully completed appointment with ID: {}", id);
        return updatedAppointment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments(Long userId) {
        return appointmentRepository.findByUserIdAndAppointmentDateAfter(userId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getPastAppointments(Long userId) {
        return appointmentRepository.findByUserIdAndAppointmentDateBefore(userId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findByAppointmentDateBetween(startDate, endDate);
    }
}
