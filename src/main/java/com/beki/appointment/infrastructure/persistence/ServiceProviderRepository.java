package com.beki.appointment.infrastructure.persistence;

import com.beki.appointment.domain.serviceprovider.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    
    List<ServiceProvider> findByServiceContainingIgnoreCase(String service);
    
    List<ServiceProvider> findByLocationContainingIgnoreCase(String location);
    
    List<ServiceProvider> findByServiceContainingIgnoreCaseAndLocationContainingIgnoreCase(String service, String location);
    
    @Query("SELECT sp FROM ServiceProvider sp ORDER BY sp.rating DESC")
    List<ServiceProvider> findTopRatedProviders();
    
    @Query(value = "SELECT sp FROM ServiceProvider sp ORDER BY sp.rating DESC LIMIT ?1", nativeQuery = true)
    List<ServiceProvider> findTopRatedProviders(int limit);
}
