package com.se114p12.backend.repository;

import com.se114p12.backend.domain.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariationOptionRepository extends JpaRepository<VariationOption, Long>,
        JpaSpecificationExecutor<VariationOption> {
    boolean existsByValue(String value);
    boolean existsByValueAndVariationId(String value, Long id);
}
