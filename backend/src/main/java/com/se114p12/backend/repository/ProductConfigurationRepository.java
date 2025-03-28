package com.se114p12.backend.repository;

import com.se114p12.backend.domain.ProductConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductConfigurationRepository extends JpaRepository<ProductConfiguration, Long> {
}