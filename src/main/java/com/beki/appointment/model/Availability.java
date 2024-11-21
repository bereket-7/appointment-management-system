package com.beki.appointment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "AVAILABILITY", indexes = {
        @Index(name = "idx_availability_id", columnList = "id, PROVIDER_ID")
})
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "PROVIDER_ID", nullable = false)
    private Long providerId;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "IS_BOOKED", nullable = false)
    private boolean isBooked = false;

}
