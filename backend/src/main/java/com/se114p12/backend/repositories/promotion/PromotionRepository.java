package com.se114p12.backend.repositories.promotion;

import com.se114p12.backend.entities.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {
    boolean existsByCode(String code);

    Promotion findByCode(String code);

    List<Promotion> findByIsPublicTrue();
}