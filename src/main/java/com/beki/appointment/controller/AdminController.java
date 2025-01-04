package com.beki.appointment.controller;

import com.beki.appointment.common.AdminDashboardDto;
import com.beki.appointment.common.AppointmentDto;
import com.beki.appointment.common.AvailabilityDto;
import com.beki.appointment.common.ServiceProviderDto;
import com.beki.appointment.service.AppointmentService;
import com.beki.appointment.service.AvailabilityService;
import com.beki.appointment.service.ServiceProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ServiceProviderService serviceProviderService;
    private final AvailabilityService availabilityService;
    private final AppointmentService appointmentService;

    public AdminController(ServiceProviderService serviceProviderService, AvailabilityService availabilityService, AppointmentService appointmentService) {
        this.serviceProviderService = serviceProviderService;
        this.availabilityService = availabilityService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/providers")
    public ResponseEntity<String> addServiceProvider(@RequestBody ServiceProviderDto serviceProviderDto) {
        serviceProviderService.addServiceProvider(serviceProviderDto);
        return ResponseEntity.ok("Service provider added successfully!");
    }

    /**
     * Get a list of all service providers.
     */
    @GetMapping("/providers")
    public ResponseEntity<List<ServiceProviderDto>> getAllServiceProviders() {
        return ResponseEntity.ok(serviceProviderService.getAllServiceProviders());
    }


    /**
     * Update an existing service provider.
     */
    @PutMapping("/providers/{id}")
    public ResponseEntity<String> updateServiceProvider(@PathVariable Long id, @RequestBody ServiceProviderDto serviceProviderDto) {
        serviceProviderService.updateServiceProvider(id, serviceProviderDto);
        return ResponseEntity.ok("Service provider updated successfully!");
    }

    /**
     * Delete a service provider.
     */
    @DeleteMapping("/providers/{id}")
    public ResponseEntity<String> deleteServiceProvider(@PathVariable Long id) {
        serviceProviderService.deleteServiceProvider(id);
        return ResponseEntity.ok("Service provider deleted successfully!");
    }

    /**
     * Get all availabilities for a service provider.
     */
    @GetMapping("/availability/{providerId}")
    public ResponseEntity<List<AvailabilityDto>> getAvailabilityByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(availabilityService.getAvailabilityByProvider(providerId));
    }

    /**
     * Delete availability by ID.
     */
    @DeleteMapping("/availability/{id}")
    public ResponseEntity<String> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok("Availability deleted successfully!");
    }

    // ==================== Appointment Management ====================

    /**
     * Get all appointments for a service provider.
     */
    @GetMapping("/appointments/provider/{providerId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByProvider(providerId));
    }

    /**
     * Get all appointments for a specific date.
     */
    @GetMapping("/appointments/date")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDate(@RequestParam Date date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }

    /**
     * Cancel an appointment by ID.
     */
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment canceled successfully!");
    }

    // ==================== Admin Dashboard ====================

    /**
     * Get a summary of system statistics (e.g., total providers, appointments, etc.).
     */
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard() {
        return ResponseEntity.ok(appointmentService.getAdminDashboard());
    }
}
