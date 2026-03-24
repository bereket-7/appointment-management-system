package com.beki.appointment.common;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AppointmentDto {
    private Long id;
    private String clientEmail;
    private String clientName;
    private Long providerId;
    private Date date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus appointmentStatus;
}
