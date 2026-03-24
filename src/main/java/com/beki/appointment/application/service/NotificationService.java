package com.beki.appointment.application.service;

import java.util.List;

public interface NotificationService {
    
    void sendAppointmentReminder(Long userId, Long appointmentId);
    
    void sendAppointmentConfirmation(Long userId, Long appointmentId);
    
    void sendAppointmentCancellation(Long userId, Long appointmentId);
    
    void sendAppointmentReschedule(Long userId, Long appointmentId);
    
    void sendWelcomeEmail(String email);
    
    void sendPasswordResetEmail(String email, String resetToken);
    
    void sendAccountVerificationEmail(String email, String verificationToken);
    
    void sendProviderWelcomeEmail(String email);
    
    void sendProviderVerificationEmail(String email);
    
    List<String> getNotificationHistory(Long userId);
    
    void markNotificationAsRead(Long notificationId);
    
    void sendBulkNotification(List<String> emails, String subject, String message);
}
