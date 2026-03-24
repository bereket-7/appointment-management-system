package com.beki.appointment.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        
        // Basic format validation
        if (!pattern.matcher(email).matches()) {
            return false;
        }
        
        // Additional checks
        return isValidEmailStructure(email);
    }
    
    private boolean isValidEmailStructure(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        
        String localPart = parts[0];
        String domain = parts[1];
        
        // Local part validation
        if (localPart.length() < 1 || localPart.length() > 64) {
            return false;
        }
        
        // Domain validation
        if (domain.length() < 4 || domain.length() > 253) {
            return false;
        }
        
        // Check for consecutive dots
        if (email.contains("..")) {
            return false;
        }
        
        // Check for leading/trailing dots
        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }
        
        return true;
    }
}
