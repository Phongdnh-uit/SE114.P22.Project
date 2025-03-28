package com.se114p12.backend.controller;

import com.se114p12.backend.domain.ProductConfiguration;
import com.se114p12.backend.service.ProductConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-configurations")
@RequiredArgsConstructor
public class ProductConfigurationController {

    private final ProductConfigurationService productConfigurationService;

    @PostMapping
    public ResponseEntity<ProductConfiguration> createConfiguration(
            @RequestParam Long productItemId,
            @RequestParam Long variationOptionId) {

        ProductConfiguration config = productConfigurationService.createProductConfiguration(productItemId, variationOptionId);
        return ResponseEntity.ok(config);
    }

    @GetMapping
    public ResponseEntity<List<ProductConfiguration>> getAllConfigurations() {
        return ResponseEntity.ok(productConfigurationService.getAllConfigurations());
    }
}