package com.beki.appointment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "URLS")
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "URL_ID")
    private Long id;

    @Column(name = "SHORT_URL",nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "ORIGINAL_URL", nullable = false, unique = true)
    private String originalUrl;

    @Column(name = "SERVICE_PROVIDER_ID")
    private Long serviceProviderId;
}
