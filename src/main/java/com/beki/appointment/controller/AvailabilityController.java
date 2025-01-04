package com.beki.appointment.controller;

import com.beki.appointment.common.AvailabilityDto;
import com.beki.appointment.model.Availability;
import com.beki.appointment.service.AvailabilityService;
import com.beki.appointment.service.ServiceProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    private final ServiceProviderService serviceProviderService;

    public AvailabilityController(AvailabilityService availabilityService, ServiceProviderService serviceProviderService) {
        this.availabilityService = availabilityService;
        this.serviceProviderService = serviceProviderService;
    }

    /**
     * Endpoint to create availability for a service provider.
     *
     * @param availabilityDto The availability details.
     * @return ResponseEntity with success message.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createAvailability(@RequestBody AvailabilityDto availabilityDto) {
        serviceProviderService.createAvailability(availabilityDto);
        return ResponseEntity.ok("Availability created successfully for provider ID: " + availabilityDto.getProviderId());
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<Availability>> getAvailableSlots(@RequestParam Long providerId) {
        List<Availability> availableSlots = availabilityService.getAvailableSlots(providerId);
        return new ResponseEntity<>(availableSlots, HttpStatus.OK);
    }
}
