package com.se114p12.backend.controllers.product;

import com.se114p12.backend.dto.product.CategoryRequestDTO;
import com.se114p12.backend.dto.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.services.product.CategoryServiceImpl;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Product Category Module")
@RequestMapping("/api/v1/product-categories")
@RestController
@RequiredArgsConstructor
public class ProductCategoryController {
    private final CategoryServiceImpl productCategoryService;

    @GetMapping
    public ResponseEntity<PageVO<CategoryResponseDTO>> getAllProductCategories(
            @ParameterObject Pageable pageable,
            @Filter Specification<ProductCategory> specification) {
        return ResponseEntity.ok(productCategoryService.getAll(specification,
                pageable.isPaged() ? pageable : Pageable.unpaged()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getProductCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(productCategoryService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryResponseDTO> createProductCategory(
            @ModelAttribute CategoryRequestDTO dto) {
        return ResponseEntity.ok(productCategoryService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryResponseDTO> updateProductCategory(
            @PathVariable Long id,
            @ModelAttribute CategoryRequestDTO dto) {
        return ResponseEntity.ok(productCategoryService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) {
        productCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }
}