package com.beki.appointment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "APPOINTMENT", indexes = {
        @Index(name = "u_uuid_index", columnList = "UUID"),
        @Index(name = "idx_appointment", columnList = "APPOINTMENT_ID, USERNAME")
})
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

    @Column(name = "TARGET_DATE")
    private Date targetDate;

    @Column(name = "DELETED", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    public Appointment() {
    }
}
