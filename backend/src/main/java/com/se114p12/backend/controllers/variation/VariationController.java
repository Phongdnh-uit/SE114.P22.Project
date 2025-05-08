package com.se114p12.backend.controllers.variation;

import com.se114p12.backend.entities.variation.Variation;
import com.se114p12.backend.services.variation.VariationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Variation Module")
@RestController
@RequestMapping("/api/v1/variations")
@RequiredArgsConstructor
public class VariationController {
    private final VariationService variationService;

    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<Variation> createVariation(@Valid @RequestBody Variation variation) {
        return ResponseEntity.ok(variationService.create(variation));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @PutMapping("/{id}")
    public ResponseEntity<Variation> updateVariation(@PathVariable("id") Long id, @RequestBody Variation variation) {
        return ResponseEntity.ok(variationService.update(id, variation));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariation(@PathVariable("id") Long id) {
        variationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
