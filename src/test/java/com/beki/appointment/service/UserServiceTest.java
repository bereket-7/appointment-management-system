package com.beki.appointment.service;

import com.beki.appointment.application.service.UserService;
import com.beki.appointment.domain.user.User;
import com.beki.appointment.domain.user.UserRole;
import com.beki.appointment.infrastructure.persistence.UserRepository;
import com.beki.appointment.interfaces.dto.UserRegistrationDto;
import com.beki.appointment.shared.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto validUserDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        validUserDto = UserRegistrationDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("SecurePass123!")
                .phone("+1234567890")
                .userRole(UserRole.ROLE_USER)
                .build();

        testUser = User.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .phone("+1234567890")
                .userRole(UserRole.ROLE_USER)
                .verified(false)
                .enabled(true)
                .deleted(false)
                .loginOtpRequired(false)
                .build();
    }

    @Test
    @DisplayName("Should register user successfully when valid data is provided")
    void registerUser_ValidData_ShouldReturnUser() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.registerUser(validUserDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(validUserDto.getEmail());
        assertThat(result.getFirstName()).isEqualTo(validUserDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(validUserDto.getLastName());
        assertThat(result.getPhone()).isEqualTo(validUserDto.getPhone());
        assertThat(result.getUserRole()).isEqualTo(validUserDto.getUserRole());

        verify(userRepository).existsByEmail(validUserDto.getEmail());
        verify(passwordEncoder).encode(validUserDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void registerUser_ExistingEmail_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.registerUser(validUserDto))
                .isInstanceOf(GeneralException.class)
                .hasMessage("Email already taken");

        verify(userRepository).existsByEmail(validUserDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should find user by email when user exists")
    void findByEmail_ExistingEmail_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail(validUserDto.getEmail())).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByEmail(validUserDto.getEmail());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(validUserDto.getEmail());

        verify(userRepository).findByEmail(validUserDto.getEmail());
    }

    @Test
    @DisplayName("Should return empty when user does not exist")
    void findByEmail_NonExistingEmail_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmail_ExistingEmail_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(true);

        // When
        boolean result = userService.existsByEmail(validUserDto.getEmail());

        // Then
        assertThat(result).isTrue();

        verify(userRepository).existsByEmail(validUserDto.getEmail());
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void existsByEmail_NonExistingEmail_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        // When
        boolean result = userService.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isFalse();

        verify(userRepository).existsByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should update user successfully when valid data is provided")
    void updateUser_ValidData_ShouldReturnUpdatedUser() {
        // Given
        UserRegistrationDto updateDto = UserRegistrationDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("+9876543210")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        User updatedUser = User.builder()
                .userId(1L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("+9876543210")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.updateUser(1L, updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getPhone()).isEqualTo("+9876543210");
        assertThat(result.getUserRole()).isEqualTo(UserRole.ROLE_ADMIN);

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing user")
    void updateUser_NonExistingUser_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, validUserDto))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully when user exists")
    void deleteUser_ExistingUser_ShouldDeleteUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing user")
    void deleteUser_NonExistingUser_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }
}
