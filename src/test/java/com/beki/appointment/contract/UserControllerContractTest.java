package com.beki.appointment.contract;

import com.beki.appointment.interfaces.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.beki.appointment.testdata.UserTestDataBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DisplayName("User Controller Contract Tests")
class UserControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/users/register - Should create user successfully")
    void registerUser_ValidData_ShouldReturn201() throws Exception {
        // Given
        UserRegistrationDto userDto = createValidUserRegistrationDto();

        // When & Then
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.data.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$.data.phone").value(userDto.getPhone()))
                .andExpect(jsonPath("$.data.userRole").value(userDto.getUserRole().toString()))
                .andExpect(jsonPath("$.data.verified").value(false))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.createdAt").exists());
    }

    @Test
    @DisplayName("POST /api/v1/users/register - Should return 400 for invalid email")
    void registerUser_InvalidEmail_ShouldReturn400() throws Exception {
        // Given
        UserRegistrationDto userDto = aUserRegistrationDto()
                .withEmail("invalid-email")
                .buildUserRegistrationDto();

        // When & Then
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email format"));
    }

    @Test
    @DisplayName("POST /api/v1/users/register - Should return 400 for weak password")
    void registerUser_WeakPassword_ShouldReturn400() throws Exception {
        // Given
        UserRegistrationDto userDto = aUserRegistrationDto()
                .withPassword("weak")
                .buildUserRegistrationDto();

        // When & Then
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Password must be at least 8 characters")));
    }

    @Test
    @DisplayName("POST /api/v1/users/register - Should return 400 for missing required fields")
    void registerUser_MissingFields_ShouldReturn400() throws Exception {
        // Given
        UserRegistrationDto userDto = UserRegistrationDto.builder()
                .firstName("John")
                // Missing lastName, email, password, phone, userRole
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/users/email/{email} - Should return user for admin")
    @WithMockUser(roles = {"ADMIN"})
    void getUserByEmail_AdminUser_ShouldReturn200() throws Exception {
        // Given
        String email = "test@example.com";

        // When & Then
        mockMvc.perform(get("/api/v1/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User found successfully"))
                .andExpect(jsonPath("$.data.email").value(email));
    }

    @Test
    @DisplayName("GET /api/v1/users/email/{email} - Should return 403 for regular user accessing other user")
    @WithMockUser(username = "different@example.com", roles = {"USER"})
    void getUserByEmail_DifferentUser_ShouldReturn403() throws Exception {
        // Given
        String email = "test@example.com";

        // When & Then
        mockMvc.perform(get("/api/v1/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/v1/users/email/{email} - Should allow user to access own data")
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void getUserByEmail_OwnUser_ShouldReturn200() throws Exception {
        // Given
        String email = "test@example.com";

        // When & Then
        mockMvc.perform(get("/api/v1/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/users - Should return all users for admin")
    @WithMockUser(roles = {"ADMIN"})
    void getAllUsers_AdminUser_ShouldReturn200() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/users - Should return 403 for regular user")
    @WithMockUser(roles = {"USER"})
    void getAllUsers_RegularUser_ShouldReturn403() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/v1/users/{userId} - Should update user successfully")
    @WithMockUser(roles = {"ADMIN"})
    void updateUser_AdminUser_ShouldReturn200() throws Exception {
        // Given
        Long userId = 1L;
        UserRegistrationDto updateDto = createValidUserRegistrationDto();

        // When & Then
        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.firstName").value(updateDto.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(updateDto.getLastName()));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{userId} - Should delete user successfully")
    @WithMockUser(roles = {"ADMIN"})
    void deleteUser_AdminUser_ShouldReturn204() throws Exception {
        // Given
        Long userId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{userId} - Should return 403 for regular user")
    @WithMockUser(roles = {"USER"})
    void deleteUser_RegularUser_ShouldReturn403() throws Exception {
        // Given
        Long userId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("All endpoints - Should return 401 for unauthenticated requests")
    void endpoints_Unauthenticated_ShouldReturn401() throws Exception {
        // Test register endpoint (should work without auth)
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest()); // 400 due to validation, not 401

        // Test protected endpoints
        mockMvc.perform(get("/api/v1/users/email/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
