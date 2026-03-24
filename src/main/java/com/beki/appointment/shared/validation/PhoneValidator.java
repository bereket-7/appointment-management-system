package com.beki.appointment.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    // Support multiple phone formats: +1234567890, (123) 456-7890, 123-456-7890, etc.
    private static final String PHONE_PATTERN = 
        "^(\\+\\d{1,3}[- ]?)?\\(?\\d{3}\\)?[- ]?\\d{3}[- ]?\\d{4}$";
    
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return false;
        }
        
        // Remove all non-digit characters for validation
        String cleanPhone = phone.replaceAll("[^\\d+]", "");
        
        // Basic length validation
        if (cleanPhone.length() < 10 || cleanPhone.length() > 15) {
            return false;
        }
        
        // Pattern validation
        return pattern.matcher(phone).matches();
    }
}
