package com.se114p12.backend.controllers.general;

import com.se114p12.backend.dto.general.PromotionRequestDTO;
import com.se114p12.backend.dto.general.PromotionResponseDTO;
import com.se114p12.backend.entities.general.Promotion;
import com.se114p12.backend.services.general.PromotionService;
import com.se114p12.backend.vo.PageVO;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Promotion Module")
@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<PageVO<PromotionResponseDTO>> getAllPromotions(
            @ParameterObject Pageable pageable,
            @Filter Specification<Promotion> specification) {
        return ResponseEntity.ok(promotionService.getAll(specification, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponseDTO> getPromotionById(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PromotionResponseDTO> createPromotion(@Valid @RequestBody PromotionRequestDTO dto) {
        return ResponseEntity.ok(promotionService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponseDTO> updatePromotion(@PathVariable Long id,
                                                                @Valid @RequestBody PromotionRequestDTO dto) {
        return ResponseEntity.ok(promotionService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.delete(id);
        return ResponseEntity.ok().build();
    }
}