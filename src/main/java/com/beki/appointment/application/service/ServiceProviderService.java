package com.beki.appointment.application.service;

import com.beki.appointment.interfaces.dto.ServiceProviderDto;
import com.beki.appointment.domain.serviceprovider.ServiceProvider;
import com.beki.appointment.domain.serviceprovider.Availability;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderService {
    
    ServiceProvider createServiceProvider(ServiceProviderDto providerDto);
    
    Optional<ServiceProvider> getServiceProviderById(Long id);
    
    List<ServiceProvider> getAllServiceProviders();
    
    ServiceProvider updateServiceProvider(Long id, ServiceProviderDto providerDto);
    
    void deleteServiceProvider(Long id);
    
    ServiceProvider addAvailability(Long providerId, Availability availability);
    
    ServiceProvider removeAvailability(Long providerId, Long availabilityId);
    
    List<Availability> getProviderAvailability(Long providerId);
    
    List<ServiceProvider> searchProviders(String service, String location);
    
    ServiceProvider updateProviderRating(Long providerId, Double rating);
    
    List<ServiceProvider> getTopRatedProviders(int limit);
}
