package com.beki.appointment.service;

import com.beki.appointment.common.AvailabilityDto;
import com.beki.appointment.common.ServiceProviderDto;
import com.beki.appointment.model.Availability;
import com.beki.appointment.model.ServiceProvider;
import com.beki.appointment.repository.AvailabilityRepository;
import com.beki.appointment.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final AvailabilityRepository availabilityRepository;

    @Autowired
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository, AvailabilityRepository availabilityRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Add a new service provider.
     */
    public void addServiceProvider(ServiceProviderDto serviceProviderDto) {
        ServiceProvider serviceProvider = mapToEntity(serviceProviderDto);
        serviceProviderRepository.save(serviceProvider);
    }

    /**
     * Get all service providers.
     */
    public List<ServiceProviderDto> getAllServiceProviders() {
        List<ServiceProvider> providers = serviceProviderRepository.findAll();
        return providers.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Update an existing service provider.
     */
    public void updateServiceProvider(Long id, ServiceProviderDto serviceProviderDto) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        serviceProvider.setName(serviceProviderDto.getName());
        serviceProvider.setEmail(serviceProviderDto.getEmail());
        serviceProvider.setPhone(serviceProviderDto.getPhone());
        serviceProvider.setSpecialization(serviceProviderDto.getSpecialization());
        serviceProviderRepository.save(serviceProvider);
    }

    /**
     * Delete a service provider by ID.
     */
    public void deleteServiceProvider(Long id) {
        serviceProviderRepository.deleteById(id);
    }

    /**
     * Map ServiceProvider entity to DTO.
     */
    private ServiceProviderDto mapToDto(ServiceProvider serviceProvider) {
        ServiceProviderDto dto = new ServiceProviderDto();
        dto.setId(serviceProvider.getId());
        dto.setName(serviceProvider.getName());
        dto.setEmail(serviceProvider.getEmail());
        dto.setPhone(serviceProvider.getPhone());
        dto.setSpecialization(serviceProvider.getSpecialization());
        return dto;
    }

    /**
     * Map ServiceProvider DTO to entity.
     */
    private ServiceProvider mapToEntity(ServiceProviderDto dto) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(dto.getName());
        serviceProvider.setEmail(dto.getEmail());
        serviceProvider.setPhone(dto.getPhone());
        serviceProvider.setSpecialization(dto.getSpecialization());
        return serviceProvider;
    }

        /**
         * Creates availability for a service provider.
         *
         * @param availabilityDto The availability details.
         */
        public void createAvailability(AvailabilityDto availabilityDto) {
            ServiceProvider serviceProvider = serviceProviderRepository.findById(availabilityDto.getProviderId())
                    .orElseThrow(() -> new IllegalArgumentException("Service Provider not found with ID: " + availabilityDto.getProviderId()));

            // Determine the day of the week
            DayOfWeek dayOfWeek = availabilityDto.getStartTime().getDayOfWeek();

            // Validate that start time is before end time
            LocalDateTime startTime = availabilityDto.getStartTime();
            LocalDateTime endTime = availabilityDto.getEndTime();
            if (startTime.isAfter(endTime)) {
                throw new IllegalArgumentException("Start time cannot be after end time.");
            }

            // Create and save availability
            Availability availability = new Availability();
            availability.setProviderId(availabilityDto.getProviderId());
            availability.setDayOfWeek(dayOfWeek);
            availability.setStartTime(startTime);
            availability.setEndTime(endTime);
            availability.setBooked(false);

            availabilityRepository.save(availability);
        }






}

