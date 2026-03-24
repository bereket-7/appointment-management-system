package com.beki.appointment.common;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AvailabilityDto {
    private Long id;
    private Long providerId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
