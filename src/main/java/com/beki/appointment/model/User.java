package com.beki.appointment.model;

import com.beki.appointment.common.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "USERS")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE USERS SET deleted = true WHERE USER_ID = ?")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", length = 20)
    private long userId;

    @Column(name = "UUID", unique = true, nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "PHONE", length = 20, nullable = false)
    private String phone;

    @Column(name = "PHOTO")
    private String photo;

    @Column(name = "RESET_PASSWORD_TOKEN")
    private String resetPasswordToken;

    @Column(name = "LOGIN_ATTEMPTED_NUMBER", nullable = false, length = 3)
    private Integer loginAttemptedNumber = 0;

    @Column(name = "BLOCKED_UNTIL")
    private Timestamp blockedUntil;

    @Column(name = "TOKEN_CREATION_TIME")
    private Timestamp tokenCreationTime;

    @Column(name = "ACCOUNT_VERIFIED", nullable = false)
    private boolean verified = false;

    @Column(name = "DELETED", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    @Column(name = "ENABLED", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Column(name = "LOGIN_OTP_REQUIRED", nullable = false)
    private boolean loginOtpRequired = false;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;
}
