package com.se114p12.backend.controller;

import com.se114p12.backend.domain.VariationOption;
import com.se114p12.backend.service.VariationOptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/variation-options")
public class VariationOptionController {
    private final VariationOptionService variationOptionService;
    public VariationOptionController(VariationOptionService variationOptionService) {
        this.variationOptionService = variationOptionService;
    }

    @PostMapping
    public ResponseEntity<VariationOption> createVariationOption(@Valid @RequestBody VariationOption variationOption) {
        return ResponseEntity.ok(variationOptionService.create(variationOption));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariationOption> updateVariationOption(
            @PathVariable Long id,
            @Valid @RequestBody VariationOption variationOption) {
        return ResponseEntity.ok(variationOptionService.update(id, variationOption));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariationOption(@PathVariable Long id) {
        variationOptionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
