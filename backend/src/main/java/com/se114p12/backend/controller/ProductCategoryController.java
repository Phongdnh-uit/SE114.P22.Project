package com.se114p12.backend.controller;

import com.se114p12.backend.domain.ProductCategory;
import com.se114p12.backend.service.ProductCategoryService;
import com.se114p12.backend.vo.PageVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Category Module")
@RequestMapping("/api/v1/product-categories")
@RestController
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<PageVO<?>> getAllProductCategories(
            Pageable pageable) {
        return ResponseEntity.ok().body(productCategoryService.getAll(pageable.isPaged() ? pageable : Pageable.unpaged()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductCategoryById(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(productCategoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProductCategory(@Valid @RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok().body(productCategoryService.create(productCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductCategory(
            @PathVariable Long id,
            @Valid @RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok().body(productCategoryService.update(id, productCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductCategory(
            @PathVariable Long id) {
        productCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

}
