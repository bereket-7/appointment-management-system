package com.beki.appointment.repository;

import com.beki.appointment.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);
    @Query("SELECT u FROM UrlMapping u " +
            "WHERE (:providerId IS NULL OR u.serviceProviderId = :providerId) " +
            "AND (:originalUrl IS NULL OR u.originalUrl LIKE %:originalUrl%) " +
            "AND (:shortUrl IS NULL OR u.shortUrl LIKE %:shortUrl%) " +
            "AND (:createdDate IS NULL OR u.createdDate = :createdDate)")
    List<UrlMapping> filterUrls(
            @Param("providerId") Long providerId,
            @Param("originalUrl") String originalUrl,
            @Param("shortUrl") String shortUrl,
            @Param("createdDate") Timestamp createdDate
    );
}
