package com.se114p12.backend.controllers.variation;

import com.se114p12.backend.domains.variation.VariationOption;
import com.se114p12.backend.services.variation.VariationOptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Variation Option Module")
@RestController
@RequestMapping("/api/v1/variation-options")
@RequiredArgsConstructor
public class VariationOptionController {
    private final VariationOptionService variationOptionService;

    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<VariationOption> createVariationOption(@Valid @RequestBody VariationOption variationOption) {
        return ResponseEntity.ok(variationOptionService.create(variationOption));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @PutMapping("/{id}")
    public ResponseEntity<VariationOption> updateVariationOption(
            @PathVariable("id") Long id,
            @Valid @RequestBody VariationOption variationOption) {
        return ResponseEntity.ok(variationOptionService.update(id, variationOption));
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariationOption(@PathVariable("id") Long id) {
        variationOptionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
