package com.beki.appointment.common;

import lombok.Data;

@Data
public class AppointmentDto {
    private Long id;
    private String clientName;
    private String providerName;
    private String date;
    private String startTime;
    private String endTime;
    private AppointmentStatus appointmentStatus;
}
