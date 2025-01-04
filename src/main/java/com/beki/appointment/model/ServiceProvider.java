package com.beki.appointment.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "service_providers")
@Data
@NoArgsConstructor
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @Column(name = "SPECIALIZATION", nullable = false)
    private String specialization;

    @Column(name = "IS_ACTIVE", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

}
