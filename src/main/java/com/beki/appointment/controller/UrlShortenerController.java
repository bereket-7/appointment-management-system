package com.beki.appointment.controller;

import com.beki.appointment.model.UrlMapping;
import com.beki.appointment.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public String createShortUrl(@RequestParam String originalUrl, @RequestParam Long serviceProviderId) {
        return urlShortenerService.createShortUrl(originalUrl, serviceProviderId);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(302).header("Location", originalUrl).build();
    }
    @DeleteMapping("/{id}")
    public void deleteShortUrl(@PathVariable Long id) {
         urlShortenerService.deleteByShortUrl(id);
    }

    @GetMapping("/filter")
    public List<UrlMapping> filterUrlMappings(
            @RequestParam(required = false) Long providerId,
            @RequestParam(required = false) String originalUrl,
            @RequestParam(required = false) String shortUrl,
            @RequestParam(required = false) Timestamp createdDate) {
        return urlShortenerService.filterUrls(providerId, originalUrl, shortUrl, createdDate);
    }
}
