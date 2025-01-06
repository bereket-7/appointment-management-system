package com.beki.appointment.service;

import com.beki.appointment.common.AdminDashboardDto;
import com.beki.appointment.common.AppointmentDto;
import com.beki.appointment.common.AppointmentStatus;
import com.beki.appointment.exception.GeneralException;
import com.beki.appointment.model.Appointment;
import com.beki.appointment.repository.AppointmentRepository;
import com.beki.appointment.repository.ServiceProviderRepository;
import com.beki.appointment.repository.UserRepository;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.beki.appointment.common.AppointmentStatus.CANCELED;
import static com.beki.appointment.common.AppointmentStatus.SCHEDULED;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository, ServiceProviderRepository serviceProviderRepository, UserRepository userRepository, NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Appointment createAppointment(AppointmentDto appointmentDto) {
        val client = userRepository.findByEmail(appointmentDto.getClientEmail())
                .orElseThrow(() -> new GeneralException("Client not found"));
        var provider = serviceProviderRepository.findById(appointmentDto.getProviderId())
                .orElseThrow(() -> new GeneralException("Provider not found"));

        Appointment appointment = new Appointment();
        appointment.setTargetDate(appointmentDto.getDate());
        appointment.setStartTime(appointmentDto.getStartTime());
        appointment.setEndTime(appointmentDto.getEndTime());
        appointment.setProvider(provider);
        appointment.setClient(client);
        appointment.setAppointmentStatus(SCHEDULED);

        // Notify the client
        String subject = "Appointment Confirmation";
        String body = String.format(
                "Dear Client, your appointment has been confirmed for %s from %s to %s.",
                appointment.getTargetDate(), appointment.getStartTime(), appointment.getEndTime()
        );
        notificationService.sendEmail(appointmentDto.getClientEmail(), subject, body);

        return appointment;
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
        dto.setClientEmail(appointment.getClient().getEmail());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setClientName(appointment.getClient().getFirstName().concat(" ").concat(appointment.getClient().getLastName()));
        dto.setProviderId(appointment.getProvider().getId());
        dto.setDate(appointment.getTargetDate());
        dto.setAppointmentStatus(appointment.getAppointmentStatus());
        return dto;
    }
}
