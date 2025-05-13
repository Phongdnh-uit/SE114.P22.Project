package com.se114p12.backend.controllers.product;

import com.se114p12.backend.dto.product.CategoryRequestDTO;
import com.se114p12.backend.dto.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.services.product.CategoryServiceImpl;
import com.se114p12.backend.vo.PageVO;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Category Module")
@RequestMapping("/api/v1/product-categories")
@RestController
@RequiredArgsConstructor
public class ProductCategoryController {
    private final CategoryServiceImpl productCategoryService;

    @GetMapping
    public ResponseEntity<PageVO<CategoryResponseDTO>> getAllProductCategories(
            @ParameterObject Pageable pageable,
            @Filter Specification<ProductCategory> specification) {
        return ResponseEntity.ok().body(productCategoryService.getAll(specification,
                pageable.isPaged() ? pageable : Pageable.unpaged()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getProductCategoryById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productCategoryService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createProductCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok().body(productCategoryService.create(categoryRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateProductCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok().body(productCategoryService.update(id, categoryRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductCategory(
            @PathVariable Long id) {
        productCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

}
