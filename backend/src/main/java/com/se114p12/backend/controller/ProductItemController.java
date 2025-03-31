package com.se114p12.backend.controller;

import com.se114p12.backend.domain.ProductItem;
import com.se114p12.backend.service.ProductItemService;
import com.se114p12.backend.vo.PageVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-items")
@RequiredArgsConstructor
public class ProductItemController {
    private final ProductItemService productItemService;

    @GetMapping
    public ResponseEntity<PageVO<ProductItem>> getAllProductItems(Pageable pageable) {
        return ResponseEntity.ok(productItemService.getAllProductItems(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductItem> getProductItemById(@PathVariable("id") Long id) {
        return productItemService.getProductItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductItem> createProductItem(@RequestBody ProductItemRequest request) {
        ProductItem createdItem = productItemService.createProductItem(request.getProductItem(), request.getVariationOptionIds());
        return ResponseEntity.ok(createdItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductItem(@PathVariable("id") Long id) {
        productItemService.deleteProductItem(id);
        return ResponseEntity.noContent().build();
    }

    // DTO để nhận dữ liệu từ request
    @Data
    public static class ProductItemRequest {
        private ProductItem productItem;
        private List<Long> variationOptionIds;
    }
}