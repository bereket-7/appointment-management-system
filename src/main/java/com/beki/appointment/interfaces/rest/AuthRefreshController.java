package com.beki.appointment.interfaces.rest;

import com.beki.appointment.interfaces.dto.TokenRefreshRequest;
import com.beki.appointment.interfaces.dto.TokenRefreshResponse;
import com.beki.appointment.infrastructure.security.JwtRefreshTokenService;
import com.beki.appointment.infrastructure.security.JwtTokenProvider;
import com.beki.appointment.shared.exception.GeneralException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRefreshController {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            // Validate refresh token
            if (!jwtRefreshTokenService.isRefreshTokenValid(refreshToken)) {
                throw new GeneralException("Invalid refresh token");
            }
            
            // Check if refresh token is expired
            if (jwtRefreshTokenService.isRefreshTokenExpired(refreshToken)) {
                throw new GeneralException("Refresh token expired");
            }
            
            // Extract email from refresh token
            String email = jwtRefreshTokenService.extractEmailFromRefreshToken(refreshToken);
            
            // Generate new access token
            String newAccessToken = jwtTokenProvider.generateToken(email);
            
            // Generate new refresh token
            String newRefreshToken = jwtRefreshTokenService.generateRefreshToken(email);
            
            TokenRefreshResponse response = new TokenRefreshResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                86400000L // 24 hours in milliseconds
            );
            
            log.info("Token refreshed successfully for user: {}", email);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
