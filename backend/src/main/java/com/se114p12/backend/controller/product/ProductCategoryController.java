package com.se114p12.backend.controller.product;

import com.se114p12.backend.domain.product.ProductCategory;
import com.se114p12.backend.service.product.ProductCategoryService;
import com.se114p12.backend.vo.PageVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Category Module")
@RequestMapping("/api/v1/product-categories")
@RestController
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<PageVO<?>> getAllProductCategories(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(productCategoryService.getAll(pageable.isPaged() ? pageable : Pageable.unpaged()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductCategoryById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productCategoryService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<?> createProductCategory(@Valid @RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok().body(productCategoryService.create(productCategory));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok().body(productCategoryService.update(id, productCategory));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductCategory(
            @PathVariable Long id) {
        productCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

}
