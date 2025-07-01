package com.se114p12.backend.repositories.product;

import com.se114p12.backend.entities.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
    Page<Product> findByCategory_IdAndDeletedFalse(Long categoryId, Pageable pageable);
}
