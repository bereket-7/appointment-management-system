package com.beki.appointment.common;

import lombok.Data;
@Data
public class AdminDashboardDto {
    private long totalProviders;
    private long totalAppointments;
    private long totalActiveAppointments;
    private long totalCompletedAppointments;
}
