package com.beki.appointment.application.service.impl;

import com.beki.appointment.application.service.UserService;
import com.beki.appointment.interfaces.dto.UserRegistrationDto;
import com.beki.appointment.domain.user.User;
import com.beki.appointment.domain.user.UserRole;
import com.beki.appointment.infrastructure.persistence.UserRepository;
import com.beki.appointment.shared.exception.GeneralException;
import com.beki.appointment.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User registerUser(UserRegistrationDto request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GeneralException("Email already taken");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setVerified(false);
        user.setDeleted(false);
        user.setEnabled(true);
        user.setLoginOtpRequired(false);
        user.setUserRole(request.getUserRole());

        User savedUser = userRepository.save(user);
        log.info("Successfully registered user with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#email")
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#email")
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public User updateUser(Long userId, UserRegistrationDto request) {
        log.info("Updating user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        if (request.getUserRole() != null) {
            user.setUserRole(request.getUserRole());
        }

        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with ID: {}", updatedUser.getUserId());
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        user.setDeleted(true);
        userRepository.save(user);
        log.info("Successfully deleted user with ID: {}", userId);
    }

    @Override
    public User changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Changing password for user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new GeneralException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);
        log.info("Successfully changed password for user with ID: {}", userId);
        return updatedUser;
    }

    @Override
    public User assignRole(Long userId, UserRole role) {
        log.info("Assigning role {} to user with ID: {}", role, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setUserRole(role);
        User updatedUser = userRepository.save(user);
        log.info("Successfully assigned role {} to user with ID: {}", role, userId);
        return updatedUser;
    }

    @Override
    public User enableUser(Long userId) {
        log.info("Enabling user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setEnabled(true);
        User updatedUser = userRepository.save(user);
        log.info("Successfully enabled user with ID: {}", userId);
        return updatedUser;
    }

    @Override
    public User disableUser(Long userId) {
        log.info("Disabling user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setEnabled(false);
        User updatedUser = userRepository.save(user);
        log.info("Successfully disabled user with ID: {}", userId);
        return updatedUser;
    }
}
