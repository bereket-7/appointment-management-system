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

    @NotBlank(message = "JWT secret cannot be blank")
    private String jwtSecret;

    @NotNull(message = "JWT expiration cannot be null")
    private Long jwtExpiration = 86400000L; // 1 day

    private Database database = new Database();
    private Mail mail = new Mail();

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
    public String getJwtSecret() { return jwtSecret; }
    public void setJwtSecret(String jwtSecret) { this.jwtSecret = jwtSecret; }
    public Long getJwtExpiration() { return jwtExpiration; }
    public void setJwtExpiration(Long jwtExpiration) { this.jwtExpiration = jwtExpiration; }
    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }
    public Mail getMail() { return mail; }
    public void setMail(Mail mail) { this.mail = mail; }
}
