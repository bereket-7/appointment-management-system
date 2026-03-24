package com.beki.appointment.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    // At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        
        // Check minimum length
        if (password.length() < 8) {
            return false;
        }
        
        // Check for common weak passwords
        if (isCommonPassword(password)) {
            return false;
        }
        
        // Pattern validation
        return pattern.matcher(password).matches();
    }
    
    private boolean isCommonPassword(String password) {
        String lowerPassword = password.toLowerCase();
        String[] commonPasswords = {
            "password", "12345678", "qwerty123", "admin123", 
            "password123", "123456789", "welcome123"
        };
        
        for (String common : commonPasswords) {
            if (lowerPassword.contains(common)) {
                return true;
            }
        }
        return false;
    }
}
