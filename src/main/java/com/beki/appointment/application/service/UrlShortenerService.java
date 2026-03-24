package com.beki.appointment.application.service;

import com.beki.appointment.domain.shared.UrlMapping;

import java.util.List;
import java.util.Optional;

public interface UrlShortenerService {
    
    String createShortUrl(String originalUrl, Long serviceProviderId);
    
    Optional<UrlMapping> getOriginalUrl(String shortUrl);
    
    Optional<UrlMapping> getUrlMappingById(Long id);
    
    List<UrlMapping> getAllUrlsByProvider(Long serviceProviderId);
    
    void deleteUrl(Long id);
    
    String generateRandomShortUrl();
    
    boolean isShortUrlAvailable(String shortUrl);
    
    void incrementClickCount(String shortUrl);
    
    List<UrlMapping> getExpiredUrls();
    
    void cleanupExpiredUrls();
}
