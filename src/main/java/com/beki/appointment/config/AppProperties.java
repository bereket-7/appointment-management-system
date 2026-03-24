package com.beki.appointment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @NotNull(message = "JWT expiration cannot be null")
    private Long jwtExpiration = 86400000L; // 1 day

    private Jwt jwt = new Jwt();
    private Database database = new Database();
    private Mail mail = new Mail();
    private RateLimit rateLimit = new RateLimit();

    public static class Jwt {
        @NotBlank(message = "JWT secret cannot be blank")
        private String secret;

        @NotNull(message = "JWT expiration cannot be null")
        private Long expirationInMs = 86400000L; // 1 day

        @NotBlank(message = "JWT refresh secret cannot be blank")
        private String refreshSecret;

        @NotNull(message = "JWT refresh expiration cannot be null")
        private Long refreshExpirationInDays = 7L; // 7 days

        // Getters and setters
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public Long getExpirationInMs() { return expirationInMs; }
        public void setExpirationInMs(Long expirationInMs) { this.expirationInMs = expirationInMs; }
        public String getRefreshSecret() { return refreshSecret; }
        public void setRefreshSecret(String refreshSecret) { this.refreshSecret = refreshSecret; }
        public Long getRefreshExpirationInDays() { return refreshExpirationInDays; }
        public void setRefreshExpirationInDays(Long refreshExpirationInDays) { this.refreshExpirationInDays = refreshExpirationInDays; }
    }

    public static class RateLimit {
        private boolean enabled = true;
        private int requestsPerMinute = 60;
        private int requestsPerHour = 1000;

        // Getters and setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getRequestsPerMinute() { return requestsPerMinute; }
        public void setRequestsPerMinute(int requestsPerMinute) { this.requestsPerMinute = requestsPerMinute; }
        public int getRequestsPerHour() { return requestsPerHour; }
        public void setRequestsPerHour(int requestsPerHour) { this.requestsPerHour = requestsPerHour; }
    }

    public static class Database {
        @NotBlank(message = "Database username cannot be blank")
        private String username;

        @NotBlank(message = "Database password cannot be blank")
        private String password;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class Mail {
        @NotBlank(message = "Mail host cannot be blank")
        private String host;

        @NotNull(message = "Mail port cannot be null")
        private Integer port;

        @NotBlank(message = "Mail username cannot be blank")
        private String username;

        @NotBlank(message = "Mail password cannot be blank")
        private String password;

        // Getters and setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public Integer getPort() { return port; }
        public void setPort(Integer port) { this.port = port; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Getters and setters
    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }
    public RateLimit getRateLimit() { return rateLimit; }
    public void setRateLimit(RateLimit rateLimit) { this.rateLimit = rateLimit; }
    public Long getJwtExpiration() { return jwtExpiration; }
    public void setJwtExpiration(Long jwtExpiration) { this.jwtExpiration = jwtExpiration; }
    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }
    public Mail getMail() { return mail; }
    public void setMail(Mail mail) { this.mail = mail; }
}
