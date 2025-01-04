package com.beki.appointment.service;

import com.beki.appointment.common.ServiceProviderDto;
import com.beki.appointment.model.ServiceProvider;
import com.beki.appointment.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    @Autowired
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
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
}

