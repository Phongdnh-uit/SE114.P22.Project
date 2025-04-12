package com.se114p12.backend.controller;

import com.se114p12.backend.domain.ProductItem;
import com.se114p12.backend.dto.request.ProductItemRequestDTO;
import com.se114p12.backend.service.ProductItemService;
import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Product Item Module")
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
    public ResponseEntity<ProductItem> createProductItem(@Valid @RequestBody ProductItemRequestDTO dto) {
        ProductItem created = productItemService.createProductItem(dto);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductItem(@PathVariable("id") Long id) {
        productItemService.deleteProductItem(id);
        return ResponseEntity.noContent().build();
    }
}