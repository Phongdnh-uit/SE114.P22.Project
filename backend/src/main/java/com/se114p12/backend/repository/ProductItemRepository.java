package com.se114p12.backend.repository;

import com.se114p12.backend.domain.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
}
