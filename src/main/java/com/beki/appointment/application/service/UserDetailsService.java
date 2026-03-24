package com.beki.appointment.application.service;

public interface UserDetailsService {
    
    com.beki.appointment.domain.user.User loadUserByUsername(String email);
    
    com.beki.appointment.domain.user.User loadUserById(Long userId);
    
    boolean isEmailVerified(String email);
    
    void markEmailAsVerified(String email);
    
    void updateLastLoginDate(String email);
    
    boolean isAccountLocked(String email);
    
    void lockAccount(String email);
    
    void unlockAccount(String email);
    
    void incrementFailedLoginAttempts(String email);
    
    void resetFailedLoginAttempts(String email);
    
    boolean isPasswordExpired(String email);
    
    void updatePasswordExpiry(String email);
}
