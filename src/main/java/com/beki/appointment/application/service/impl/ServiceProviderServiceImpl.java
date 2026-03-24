package com.beki.appointment.application.service.impl;

import com.beki.appointment.application.service.ServiceProviderService;
import com.beki.appointment.interfaces.dto.ServiceProviderDto;
import com.beki.appointment.domain.serviceprovider.ServiceProvider;
import com.beki.appointment.domain.serviceprovider.Availability;
import com.beki.appointment.infrastructure.persistence.ServiceProviderRepository;
import com.beki.appointment.infrastructure.persistence.AvailabilityRepository;
import com.beki.appointment.shared.exception.GeneralException;
import com.beki.appointment.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final AvailabilityRepository availabilityRepository;

    @Override
    public ServiceProvider createServiceProvider(ServiceProviderDto providerDto) {
        log.info("Creating new service provider: {}", providerDto.getName());
        
        ServiceProvider provider = new ServiceProvider();
        provider.setName(providerDto.getName());
        provider.setEmail(providerDto.getEmail());
        provider.setPhone(providerDto.getPhone());
        provider.setService(providerDto.getService());
        provider.setLocation(providerDto.getLocation());
        provider.setDescription(providerDto.getDescription());
        provider.setRating(0.0);
        provider.setVerified(false);

        ServiceProvider savedProvider = serviceProviderRepository.save(provider);
        log.info("Successfully created service provider with ID: {}", savedProvider.getProviderId());
        return savedProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceProvider> getServiceProviderById(Long id) {
        return serviceProviderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    @Override
    public ServiceProvider updateServiceProvider(Long id, ServiceProviderDto providerDto) {
        log.info("Updating service provider with ID: {}", id);
        
        ServiceProvider provider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + id));

        provider.setName(providerDto.getName());
        provider.setEmail(providerDto.getEmail());
        provider.setPhone(providerDto.getPhone());
        provider.setService(providerDto.getService());
        provider.setLocation(providerDto.getLocation());
        provider.setDescription(providerDto.getDescription());

        ServiceProvider updatedProvider = serviceProviderRepository.save(provider);
        log.info("Successfully updated service provider with ID: {}", updatedProvider.getProviderId());
        return updatedProvider;
    }

    @Override
    public void deleteServiceProvider(Long id) {
        log.info("Deleting service provider with ID: {}", id);
        
        ServiceProvider provider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + id));

        serviceProviderRepository.delete(provider);
        log.info("Successfully deleted service provider with ID: {}", id);
    }

    @Override
    public ServiceProvider addAvailability(Long providerId, Availability availability) {
        log.info("Adding availability for service provider with ID: {}", providerId);
        
        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + providerId));

        availability.setProviderId(providerId);
        availabilityRepository.save(availability);

        log.info("Successfully added availability for service provider with ID: {}", providerId);
        return provider;
    }

    @Override
    public ServiceProvider removeAvailability(Long providerId, Long availabilityId) {
        log.info("Removing availability {} for service provider with ID: {}", availabilityId, providerId);
        
        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + providerId));

        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + availabilityId));

        if (!availability.getProviderId().equals(providerId)) {
            throw new GeneralException("Availability does not belong to this service provider");
        }

        availabilityRepository.delete(availability);
        log.info("Successfully removed availability {} for service provider with ID: {}", availabilityId, providerId);
        return provider;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getProviderAvailability(Long providerId) {
        return availabilityRepository.findByProviderIdAndIsBookedFalse(providerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceProvider> searchProviders(String service, String location) {
        if (service != null && location != null) {
            return serviceProviderRepository.findByServiceContainingIgnoreCaseAndLocationContainingIgnoreCase(service, location);
        } else if (service != null) {
            return serviceProviderRepository.findByServiceContainingIgnoreCase(service);
        } else if (location != null) {
            return serviceProviderRepository.findByLocationContainingIgnoreCase(location);
        } else {
            return serviceProviderRepository.findAll();
        }
    }

    @Override
    public ServiceProvider updateProviderRating(Long providerId, Double rating) {
        log.info("Updating rating for service provider with ID: {} to {}", providerId, rating);
        
        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + providerId));

        if (rating < 0.0 || rating > 5.0) {
            throw new GeneralException("Rating must be between 0.0 and 5.0");
        }

        provider.setRating(rating);
        ServiceProvider updatedProvider = serviceProviderRepository.save(provider);
        log.info("Successfully updated rating for service provider with ID: {}", providerId);
        return updatedProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceProvider> getTopRatedProviders(int limit) {
        return serviceProviderRepository.findTopRatedProviders(limit);
    }
}
