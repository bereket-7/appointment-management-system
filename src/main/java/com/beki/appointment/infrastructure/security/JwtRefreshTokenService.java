package com.beki.appointment.infrastructure.security;

import com.beki.appointment.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRefreshTokenService {

    private final AppProperties appProperties;
    private SecretKey refreshKey;

    @PostConstruct
    public void init() {
        this.refreshKey = Keys.hmacShaKeyFor(appProperties.getJwt().getRefreshSecret().getBytes());
    }

    public String generateRefreshToken(String email) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(appProperties.getJwt().getRefreshExpirationInDays(), ChronoUnit.DAYS);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractEmailFromRefreshToken(String refreshToken) {
        Claims claims = extractAllClaims(refreshToken);
        return claims.getSubject();
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            extractAllClaims(refreshToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isRefreshTokenExpired(String refreshToken) {
        try {
            Claims claims = extractAllClaims(refreshToken);
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    private Claims extractAllClaims(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
    }

    public Date getRefreshTokenExpiration(String refreshToken) {
        Claims claims = extractAllClaims(refreshToken);
        return claims.getExpiration();
    }
}
