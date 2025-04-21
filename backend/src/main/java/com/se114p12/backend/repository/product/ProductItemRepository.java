package com.se114p12.backend.repository.product;

import com.se114p12.backend.domain.product.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
}
