package com.beki.appointment.repository;

import com.beki.appointment.domain.user.User;
import com.beki.appointment.domain.user.UserRole;
import com.beki.appointment.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("User Repository Integration Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
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
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void save_ValidUser_ShouldPersistUser() {
        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUser.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(testUser.getLastName());
    }

    @Test
    @DisplayName("Should find user by email when user exists")
    void findByEmail_ExistingEmail_ShouldReturnUser() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> foundUser = userRepository.findByEmail(testUser.getEmail());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(testUser.getEmail());
        assertThat(foundUser.get().getUserId()).isEqualTo(testUser.getUserId());
    }

    @Test
    @DisplayName("Should return empty when user does not exist")
    void findByEmail_NonExistingEmail_ShouldReturnEmpty() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmail_ExistingEmail_ShouldReturnTrue() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        boolean exists = userRepository.existsByEmail(testUser.getEmail());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void existsByEmail_NonExistingEmail_ShouldReturnFalse() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find user by ID when user exists")
    void findById_ExistingId_ShouldReturnUser() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getUserId());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(foundUser.get().getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    @DisplayName("Should return empty when user ID does not exist")
    void findById_NonExistingId_ShouldReturnEmpty() {
        // When
        Optional<User> foundUser = userRepository.findById(999L);

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should find all users")
    void findAll_ShouldReturnAllUsers() {
        // Given
        User user2 = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("encodedPassword")
                .phone("+9876543210")
                .userRole(UserRole.ROLE_ADMIN)
                .verified(true)
                .enabled(true)
                .deleted(false)
                .loginOtpRequired(false)
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(user2);

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder(testUser.getEmail(), user2.getEmail());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void delete_ExistingUser_ShouldRemoveUser() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);

        // When
        userRepository.delete(savedUser);
        entityManager.flush();

        // Then
        Optional<User> deletedUser = userRepository.findById(savedUser.getUserId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    @DisplayName("Should find users by role")
    void findByUserRole_ExistingRole_ShouldReturnUsersWithRole() {
        // Given
        User adminUser = User.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin@example.com")
                .password("encodedPassword")
                .phone("+1111111111")
                .userRole(UserRole.ROLE_ADMIN)
                .verified(true)
                .enabled(true)
                .deleted(false)
                .loginOtpRequired(false)
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(testUser); // ROLE_USER
        entityManager.persistAndFlush(adminUser); // ROLE_ADMIN

        // When
        List<User> adminUsers = userRepository.findByUserRole(UserRole.ROLE_ADMIN);

        // Then
        assertThat(adminUsers).hasSize(1);
        assertThat(adminUsers.get(0).getUserRole()).isEqualTo(UserRole.ROLE_ADMIN);
        assertThat(adminUsers.get(0).getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("Should find enabled users only")
    void findByEnabledTrue_ShouldReturnOnlyEnabledUsers() {
        // Given
        User disabledUser = User.builder()
                .firstName("Disabled")
                .lastName("User")
                .email("disabled@example.com")
                .password("encodedPassword")
                .phone("+2222222222")
                .userRole(UserRole.ROLE_USER)
                .verified(true)
                .enabled(false) // Disabled
                .deleted(false)
                .loginOtpRequired(false)
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(testUser); // Enabled
        entityManager.persistAndFlush(disabledUser); // Disabled

        // When
        List<User> enabledUsers = userRepository.findByEnabledTrue();

        // Then
        assertThat(enabledUsers).hasSize(1);
        assertThat(enabledUsers.get(0).isEnabled()).isTrue();
        assertThat(enabledUsers.get(0).getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("Should find verified users only")
    void findByVerifiedTrue_ShouldReturnOnlyVerifiedUsers() {
        // Given
        User unverifiedUser = User.builder()
                .firstName("Unverified")
                .lastName("User")
                .email("unverified@example.com")
                .password("encodedPassword")
                .phone("+3333333333")
                .userRole(UserRole.ROLE_USER)
                .verified(false) // Unverified
                .enabled(true)
                .deleted(false)
                .loginOtpRequired(false)
                .createdAt(LocalDateTime.now())
                .build();

        testUser.setVerified(true); // Verified
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(unverifiedUser);

        // When
        List<User> verifiedUsers = userRepository.findByVerifiedTrue();

        // Then
        assertThat(verifiedUsers).hasSize(1);
        assertThat(verifiedUsers.get(0).isVerified()).isTrue();
        assertThat(verifiedUsers.get(0).getEmail()).isEqualTo(testUser.getEmail());
    }
}
