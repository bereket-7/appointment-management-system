package com.beki.appointment.application.service;

import com.beki.appointment.interfaces.dto.UserRegistrationDto;
import com.beki.appointment.domain.user.User;
import com.beki.appointment.domain.user.UserRole;

import java.util.Optional;

public interface UserService {
    
    User registerUser(UserRegistrationDto request);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    User updateUser(Long userId, UserRegistrationDto request);
    
    void deleteUser(Long userId);
    
    User changePassword(Long userId, String oldPassword, String newPassword);
    
    User assignRole(Long userId, UserRole role);
    
    User enableUser(Long userId);
    
    User disableUser(Long userId);
}
