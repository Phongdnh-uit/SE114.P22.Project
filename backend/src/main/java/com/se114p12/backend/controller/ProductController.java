package com.se114p12.backend.controller;

import com.se114p12.backend.domain.Product;
import com.se114p12.backend.service.ProductService;

import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Module")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/category/{id}")
    public ResponseEntity<PageVO<Product>> getProductsByCategory(
            @PathVariable("id") Long categoryId, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody Product productDetails) {
        return ResponseEntity.ok(productService.update(id, productDetails));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
