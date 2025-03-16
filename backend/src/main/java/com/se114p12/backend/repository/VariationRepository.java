package com.se114p12.backend.repository;

import com.se114p12.backend.domain.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VariationRepository extends JpaRepository<Variation, Long>, JpaSpecificationExecutor<Variation> {
    boolean existsByName(String name);
}
