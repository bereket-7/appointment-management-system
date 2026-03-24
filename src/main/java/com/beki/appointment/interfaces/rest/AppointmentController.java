package com.beki.appointment.interfaces.rest;

import com.beki.appointment.interfaces.dto.AppointmentDto;
import com.beki.appointment.domain.appointment.Appointment;
import com.beki.appointment.application.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.createAppointment(appointmentDto);

    }
}
