package com.beki.appointment.common;

import lombok.Data;

@Data
public class ServiceProviderDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
}
