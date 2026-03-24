package com.beki.appointment.testdata;

import com.beki.appointment.domain.user.User;
import com.beki.appointment.domain.user.UserRole;
import com.beki.appointment.interfaces.dto.UserRegistrationDto;

import java.time.LocalDateTime;

/**
 * Test data builder for User and UserRegistrationDto objects.
 * Provides fluent API for creating test data with default values.
 */
public class UserTestDataBuilder {

    private Long userId;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String password = "SecurePass123!";
    private String phone = "+1234567890";
    private UserRole userRole = UserRole.ROLE_USER;
    private boolean verified = false;
    private boolean enabled = true;
    private boolean deleted = false;
    private boolean loginOtpRequired = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    private UserTestDataBuilder() {}

    public static UserTestDataBuilder aUser() {
        return new UserTestDataBuilder();
    }

    public static UserTestDataBuilder aUserRegistrationDto() {
        return new UserTestDataBuilder();
    }

    public UserTestDataBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UserTestDataBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserTestDataBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserTestDataBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserTestDataBuilder withRole(UserRole userRole) {
        this.userRole = userRole;
        return this;
    }

    public UserTestDataBuilder verified() {
        this.verified = true;
        return this;
    }

    public UserTestDataBuilder notVerified() {
        this.verified = false;
        return this;
    }

    public UserTestDataBuilder enabled() {
        this.enabled = true;
        return this;
    }

    public UserTestDataBuilder disabled() {
        this.enabled = false;
        return this;
    }

    public UserTestDataBuilder deleted() {
        this.deleted = true;
        return this;
    }

    public UserTestDataBuilder withLoginOtpRequired() {
        this.loginOtpRequired = true;
        return this;
    }

    public UserTestDataBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User build() {
        return User.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phone(phone)
                .userRole(userRole)
                .verified(verified)
                .enabled(enabled)
                .deleted(deleted)
                .loginOtpRequired(loginOtpRequired)
                .createdAt(createdAt)
                .build();
    }

    public UserRegistrationDto buildUserRegistrationDto() {
        return UserRegistrationDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phone(phone)
                .userRole(userRole)
                .build();
    }

    // Predefined test data methods
    public static UserTestDataBuilder anAdminUser() {
        return aUser()
                .withRole(UserRole.ROLE_ADMIN)
                .verified()
                .enabled();
    }

    public static UserTestDataBuilder aServiceProvider() {
        return aUser()
                .withRole(UserRole.ROLE_SERVICE_PROVIDER)
                .verified()
                .enabled();
    }

    public static UserTestDataBuilder aRegularUser() {
        return aUser()
                .withRole(UserRole.ROLE_USER)
                .notVerified()
                .enabled();
    }

    public static UserTestDataBuilder aDisabledUser() {
        return aUser()
                .withRole(UserRole.ROLE_USER)
                .verified()
                .disabled();
    }

    public static UserTestDataBuilder aDeletedUser() {
        return aUser()
                .withRole(UserRole.ROLE_USER)
                .verified()
                .enabled()
                .deleted();
    }

    // Convenience methods for common scenarios
    public static User createValidUser() {
        return aUser().build();
    }

    public static User createAdminUser() {
        return anAdminUser().build();
    }

    public static User createServiceProvider() {
        return aServiceProvider().build();
    }

    public static UserRegistrationDto createValidUserRegistrationDto() {
        return aUserRegistrationDto().buildUserRegistrationDto();
    }

    public static UserRegistrationDto createAdminRegistrationDto() {
        return anAdminUser().buildUserRegistrationDto();
    }

    public static UserRegistrationDto createServiceProviderRegistrationDto() {
        return aServiceProvider().buildUserRegistrationDto();
    }
}
