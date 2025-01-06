package com.beki.appointment.controller;

import com.beki.appointment.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        notificationService.sendEmail(to, subject, body);
        return ResponseEntity.ok("Email sent successfully to " + to);
    }
}
