package com.beki.appointment.interfaces.rest.v1;

import com.beki.appointment.interfaces.dto.UserRegistrationDto;
import com.beki.appointment.application.service.UserService;
import com.beki.appointment.domain.user.User;
import com.beki.appointment.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management V1", description = "User management operations with version 1 API")
@SecurityRequirement(name = "bearer-jwt")
public class UserControllerV1 {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "UserCreatedExample",
                                    value = """
                                            {
                                                "userId": 1,
                                                "firstName": "John",
                                                "lastName": "Doe",
                                                "email": "john.doe@example.com",
                                                "phone": "+1234567890",
                                                "userRole": "ROLE_USER",
                                                "verified": false,
                                                "enabled": true,
                                                "createdAt": "2024-03-24T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ValidationErrorExample",
                                    value = """
                                            {
                                                "timestamp": "2024-03-24T10:30:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "message": "Email format is invalid",
                                                "path": "/api/v1/users/register"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ConflictExample",
                                    value = """
                                            {
                                                "timestamp": "2024-03-24T10:30:00",
                                                "status": 409,
                                                "error": "Conflict",
                                                "message": "Email already taken",
                                                "path": "/api/v1/users/register"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(
            @Valid @RequestBody 
            @Parameter(
                    description = "User registration information",
                    required = true,
                    example = """
                            {
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "john.doe@example.com",
                                "password": "SecurePass123!",
                                "phone": "+1234567890",
                                "userRole": "ROLE_USER"
                            }
                            """
            )
            UserRegistrationDto request) {
        
        log.info("Registering new user with email: {}", request.getEmail());
        User createdUser = userService.registerUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<User>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(createdUser)
                        .build());
    }

    @Operation(
            summary = "Get user by email",
            description = "Retrieves user information by email address"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.name")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(
            @Parameter(
                    description = "User email address",
                    required = true,
                    example = "john.doe@example.com"
            )
            @PathVariable String email) {
        
        Optional<User> user = userService.findByEmail(email);
        
        return user.map(u -> ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .success(true)
                        .message("User found successfully")
                        .data(u)
                        .build()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<User>builder()
                                .success(false)
                                .message("User not found")
                                .build()));
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Admin role required"
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        
        return ResponseEntity.ok(
                ApiResponse.<List<User>>builder()
                        .success(true)
                        .message("Users retrieved successfully")
                        .data(users)
                        .build());
    }

    @Operation(
            summary = "Update user",
            description = "Updates user information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @Parameter(
                    description = "User ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long userId,
            @Valid @RequestBody UserRegistrationDto request) {
        
        User updatedUser = userService.updateUser(userId, request);
        
        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .success(true)
                        .message("User updated successfully")
                        .data(updatedUser)
                        .build());
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user account (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Admin role required"
            )
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(
                    description = "User ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long userId) {
        
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
