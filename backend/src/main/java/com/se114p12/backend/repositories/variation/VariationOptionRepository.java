package com.se114p12.backend.repositories.variation;

import com.se114p12.backend.entities.variation.VariationOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariationOptionRepository extends JpaRepository<VariationOption, Long>,
        JpaSpecificationExecutor<VariationOption> {
    boolean existsByValueAndVariationId(String value, Long id);
    Page<VariationOption> findByVariationId(Long variationId, Pageable pageable);
}
