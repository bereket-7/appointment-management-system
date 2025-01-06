package com.beki.appointment.model;

import com.beki.appointment.common.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "APPOINTMENT")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE APPOINTMENT SET deleted = true WHERE APPOINTMENT_ID = ?")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APPOINTMENT_ID")
    private long appointmentId;

    @Column(name = "USERNAME",nullable = false)
    private String username;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPOINTMENT_STATUS", nullable = false)
    private AppointmentStatus appointmentStatus;

    @Column(name = "TARGET_DATE")
    private Date targetDate;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Column(name = "DELETED", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

    public Appointment() {
    }
}
