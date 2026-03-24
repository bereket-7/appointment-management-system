package com.beki.appointment.interfaces.mapper;

import com.beki.appointment.domain.serviceprovider.ServiceProvider;
import com.beki.appointment.interfaces.dto.ServiceProviderDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceProviderMapper {

    public ServiceProviderDto toDto(ServiceProvider provider) {
        if (provider == null) {
            return null;
        }

        ServiceProviderDto dto = new ServiceProviderDto();
        dto.setProviderId(provider.getProviderId());
        dto.setName(provider.getName());
        dto.setEmail(provider.getEmail());
        dto.setPhone(provider.getPhone());
        dto.setService(provider.getService());
        dto.setLocation(provider.getLocation());
        dto.setDescription(provider.getDescription());
        dto.setRating(provider.getRating());
        dto.setVerified(provider.isVerified());
        dto.setCreationDate(provider.getCreationDate());
        dto.setLastUpdateDate(provider.getLastUpdateDate());

        return dto;
    }

    public ServiceProvider toEntity(ServiceProviderDto dto) {
        if (dto == null) {
            return null;
        }

        ServiceProvider provider = new ServiceProvider();
        provider.setName(dto.getName());
        provider.setEmail(dto.getEmail());
        provider.setPhone(dto.getPhone());
        provider.setService(dto.getService());
        provider.setLocation(dto.getLocation());
        provider.setDescription(dto.getDescription());
        provider.setRating(dto.getRating());
        provider.setVerified(dto.isVerified());

        return provider;
    }

    public void updateEntity(ServiceProvider provider, ServiceProviderDto dto) {
        if (dto == null || provider == null) {
            return;
        }

        if (dto.getName() != null) {
            provider.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            provider.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            provider.setPhone(dto.getPhone());
        }
        if (dto.getService() != null) {
            provider.setService(dto.getService());
        }
        if (dto.getLocation() != null) {
            provider.setLocation(dto.getLocation());
        }
        if (dto.getDescription() != null) {
            provider.setDescription(dto.getDescription());
        }
        if (dto.getRating() != null) {
            provider.setRating(dto.getRating());
        }
        if (dto.isVerified() != provider.isVerified()) {
            provider.setVerified(dto.isVerified());
        }
    }

    public List<ServiceProviderDto> toDtoList(List<ServiceProvider> providers) {
        return providers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ServiceProvider> toEntityList(List<ServiceProviderDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
