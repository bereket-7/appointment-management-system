package com.beki.appointment.controller;

import com.beki.appointment.model.Availability;
import com.beki.appointment.service.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping("/create")
    public ResponseEntity<Availability> createAvailability(
            @RequestParam Long providerId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        Availability availability = availabilityService.createAvailability(providerId, startTime, endTime);
        return new ResponseEntity<>(availability, HttpStatus.CREATED);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<Availability>> getAvailableSlots(@RequestParam Long providerId) {
        List<Availability> availableSlots = availabilityService.getAvailableSlots(providerId);
        return new ResponseEntity<>(availableSlots, HttpStatus.OK);
    }
}
