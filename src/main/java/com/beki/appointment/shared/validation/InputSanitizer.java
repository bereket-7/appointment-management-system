package com.beki.appointment.shared.validation;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

    /**
     * Sanitizes input string to prevent XSS attacks
     */
    public String sanitize(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove potentially dangerous characters and patterns
        String sanitized = input.trim();
        
        // HTML encode to prevent XSS
        sanitized = Encode.forHtml(sanitized);
        
        // Remove script tags and event handlers
        sanitized = sanitized.replaceAll("(?i)<script.*?>.*?</script>", "");
        sanitized = sanitized.replaceAll("(?i)on\\w+\\s*=\\s*[\"'].*?[\"']", "");
        sanitized = sanitized.replaceAll("(?i)javascript:", "");
        
        // Remove SQL injection patterns
        sanitized = sanitized.replaceAll("(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute)", "");
        sanitized = sanitized.replaceAll("(?i)[\"';]", "");
        
        return sanitized;
    }
    
    /**
     * Sanitizes email addresses
     */
    public String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        
        String sanitized = email.toLowerCase().trim();
        // Remove potentially dangerous characters from email
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9@._-]", "");
        
        return sanitized;
    }
    
    /**
     * Sanitizes phone numbers
     */
    public String sanitizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        
        // Keep only digits, plus, hyphen, and parentheses
        String sanitized = phone.replaceAll("[^0-9+()-]", "");
        
        return sanitized;
    }
    
    /**
     * Validates that input doesn't contain malicious patterns
     */
    public boolean isSafe(String input) {
        if (input == null) {
            return true;
        }
        
        String lowerInput = input.toLowerCase();
        
        // Check for common XSS patterns
        String[] xssPatterns = {
            "<script", "</script>", "javascript:", "onload=", "onerror=",
            "onclick=", "onmouseover=", "onfocus=", "onblur=", "onchange=",
            "onsubmit=", "onreset=", "eval(", "expression("
        };
        
        for (String pattern : xssPatterns) {
            if (lowerInput.contains(pattern)) {
                return false;
            }
        }
        
        // Check for SQL injection patterns
        String[] sqlPatterns = {
            "union select", "drop table", "delete from", "insert into",
            "update set", "create table", "alter table", "exec(", "execute("
        };
        
        for (String pattern : sqlPatterns) {
            if (lowerInput.contains(pattern)) {
                return false;
            }
        }
        
        return true;
    }
}
