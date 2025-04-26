package com.se114p12.backend.repository.variation;

import com.se114p12.backend.domains.variation.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariationRepository extends JpaRepository<Variation, Long>,
        JpaSpecificationExecutor<Variation> {
    boolean existsByName(String name);
}
