package com.se114p12.backend.controller;

import com.se114p12.backend.domain.Variation;
import com.se114p12.backend.service.VariationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Variation Module")
@RestController
@RequestMapping("/api/v1/variations")
public class VariationController {
    private final VariationService variationService;

    public VariationController(VariationService variationService) {
        this.variationService = variationService;
    }

    @PostMapping
    public ResponseEntity<Variation> createVariation(@Valid @RequestBody Variation variation) {
        return ResponseEntity.ok(variationService.create(variation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variation> updateVariation(@PathVariable Long id, @RequestBody Variation variation) {
        return ResponseEntity.ok(variationService.update(id, variation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariation(@PathVariable Long id) {
        variationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
