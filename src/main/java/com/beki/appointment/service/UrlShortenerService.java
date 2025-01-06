package com.beki.appointment.service;

import com.beki.appointment.model.UrlMapping;
import com.beki.appointment.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.beki.appointment.common.AppConstants.URL_CHARACTERS;

@Service
public class UrlShortenerService {

    private final UrlRepository urlMappingRepository;
    public UrlShortenerService(UrlRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public String createShortUrl(String originalUrl, Long serviceProviderId) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setServiceProviderId(serviceProviderId);
        urlMappingRepository.save(urlMapping);
        return  ("Shortened URL: http://appointment-app.com/" + shortUrl) + shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (mapping.isEmpty()) {
            throw new IllegalArgumentException("Short URL not found.");
        }
        return mapping.get().getOriginalUrl();
    }

    private String generateShortUrl() {
        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            shortUrl.append(URL_CHARACTERS.charAt(random.nextInt(URL_CHARACTERS.length())));
        }
        return shortUrl.toString();
    }

    public void  deleteByShortUrl(Long id) {
         urlMappingRepository.deleteById(id);
    }

    public List<UrlMapping> filterUrls(Long providerId, String originalUrl, String shortUrl, Timestamp createdDate) {
        return urlMappingRepository.filterUrls(providerId, originalUrl, shortUrl, createdDate);
    }
}
