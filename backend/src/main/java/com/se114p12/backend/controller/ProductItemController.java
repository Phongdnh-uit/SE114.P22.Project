package com.se114p12.backend.controller;

import com.se114p12.backend.domain.ProductItem;
import com.se114p12.backend.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-items")
@RequiredArgsConstructor
public class ProductItemController {
    private final ProductItemService productItemService;

    @GetMapping
    public ResponseEntity<List<ProductItem>> getAllProductItems() {
        return ResponseEntity.ok(productItemService.getAllProductItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductItem> getProductItemById(@PathVariable("id") Long id) {
        return productItemService.getProductItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductItem> createProductItem(@RequestBody ProductItem productItem) {
        return ResponseEntity.ok(productItemService.saveProductItem(productItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductItem(@PathVariable("id") Long id) {
        productItemService.deleteProductItem(id);
        return ResponseEntity.noContent().build();
    }
}