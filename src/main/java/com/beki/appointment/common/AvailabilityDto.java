package com.beki.appointment.common;

import lombok.Data;

@Data
public class AvailabilityDto {
    private Long id;
    private Long providerId;
    private String date;
    private String startTime;
    private String endTime;
    private int slotDuration;
}
