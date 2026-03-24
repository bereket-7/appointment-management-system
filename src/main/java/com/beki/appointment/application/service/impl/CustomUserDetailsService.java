package com.beki.appointment.application.service.impl;

import com.beki.appointment.domain.user.User;
import com.beki.appointment.infrastructure.persistence.UserRepository;
import com.beki.appointment.infrastructure.security.CustomUserDetails;
import com.beki.appointment.application.service.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService, org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public com.beki.appointment.domain.user.User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public com.beki.appointment.domain.user.User loadUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public boolean isEmailVerified(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        return user.isVerified();
    }

    @Override
    public void markEmailAsVerified(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public void updateLastLoginDate(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        user.setLastLoginDate(java.time.LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean isAccountLocked(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        return !user.isEnabled();
    }

    @Override
    public void lockAccount(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void unlockAccount(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void incrementFailedLoginAttempts(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        userRepository.save(user);
    }

    @Override
    public void resetFailedLoginAttempts(String email) {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        user.setLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public boolean isPasswordExpired(String email) {
        // TODO: Implement password expiry logic
        return false;
    }

    @Override
    public void updatePasswordExpiry(String email) {
        // TODO: Implement password expiry update logic
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.beki.appointment.domain.user.User user = loadUserByUsername(email);
        return new CustomUserDetails(user);
    }
}
