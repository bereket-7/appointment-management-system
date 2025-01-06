package com.beki.appointment.controller;

import com.beki.appointment.common.AppointmentDto;
import com.beki.appointment.model.Appointment;
import com.beki.appointment.service.AppointmentService;
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
