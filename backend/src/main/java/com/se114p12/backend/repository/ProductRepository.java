package com.se114p12.backend.repository;

import com.se114p12.backend.domain.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);
    boolean existsByShortDescription(String description);

    Product findByName(String name);

    List<Product> findProductByNameContainingIgnoreCaseAndIsAvailable(String name, Boolean isAvailable);

    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
}
