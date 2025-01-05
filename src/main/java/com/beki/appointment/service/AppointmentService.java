package com.beki.appointment.service;

import com.beki.appointment.common.AdminDashboardDto;
import com.beki.appointment.common.AppointmentDto;
import com.beki.appointment.common.AppointmentStatus;
import com.beki.appointment.exception.GeneralException;
import com.beki.appointment.model.Appointment;
import com.beki.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.beki.appointment.common.AppointmentStatus.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Get appointments by service provider ID.
     */
    public List<AppointmentDto> getAppointmentsByProvider(Long providerId) {
        List<Appointment> appointments = appointmentRepository.findByProviderId(providerId);
        return appointments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Get appointments by date.
     */
    public List<AppointmentDto> getAppointmentsByDate(Date date) {
        List<Appointment> appointments = appointmentRepository.findByTargetDate(date);
        return appointments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Cancel an appointment by ID.
     */
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GeneralException("Appointment not found"));
        appointment.setAppointmentStatus(CANCELED);
        appointmentRepository.save(appointment);
    }

    /**
     * Get admin dashboard summary.
     */
    public AdminDashboardDto getAdminDashboard(AppointmentStatus appointmentStatus) {
        long totalProviders = appointmentRepository.countDistinctProviders();
        long totalAppointments = appointmentRepository.count();
        long totalActiveAppointments = appointmentRepository.countByAppointmentStatus(appointmentStatus);
        long totalCompletedAppointments = appointmentRepository.countByAppointmentStatus(appointmentStatus);

        AdminDashboardDto dashboardDto = new AdminDashboardDto();
        dashboardDto.setTotalProviders(totalProviders);
        dashboardDto.setTotalAppointments(totalAppointments);
        dashboardDto.setTotalActiveAppointments(totalActiveAppointments);
        dashboardDto.setTotalCompletedAppointments(totalCompletedAppointments);

        return dashboardDto;
    }

    /**
     * Map Appointment entity to DTO.
     */
    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getAppointmentId());
        dto.setProviderName(appointment.getProvider().getName());
        dto.setDate(appointment.getTargetDate().toString());
        dto.setAppointmentStatus(appointment.getAppointmentStatus());
        return dto;
    }
}
