package com.se114p12.backend.controllers.product;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.product.CategoryRequestDTO;
import com.se114p12.backend.dtos.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.services.product.CategoryService;
import com.se114p12.backend.vo.PageVO;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Category Module", description = "APIs for managing product categories")
@RequestMapping(AppConstant.API_BASE_PATH + "/product-categories")
@RestController
@RequiredArgsConstructor
public class ProductCategoryController {
  private final CategoryService productCategoryService;

  @Operation(
      summary = "Get all product categories",
      description = "Retrieve a list of all product categories.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved product categories"),
      })
  @ErrorResponse
  @GetMapping
  public ResponseEntity<PageVO<CategoryResponseDTO>> getAllProductCategories(
      @ParameterObject Pageable pageable,
      @Filter @Parameter(name = "filter") Specification<ProductCategory> specification) {
    return ResponseEntity.ok(
        productCategoryService.getAll(
            specification, pageable.isPaged() ? pageable : Pageable.unpaged()));
  }

  @Operation(
      summary = "Get product category by ID",
      description = "Retrieve a product category by its ID.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved product category",
      content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
  @ErrorResponse
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDTO> getProductCategoryById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(productCategoryService.findById(id));
  }

  @Operation(
      summary = "Create a new product category",
      description = "Create a new product category.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully created product category",
      content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<CategoryResponseDTO> createProductCategory(
      @Valid @ModelAttribute CategoryRequestDTO dto) {
    return ResponseEntity.ok(productCategoryService.create(dto));
  }

  @Operation(
      summary = "Update a product category",
      description = "Update an existing product category.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully updated product category",
      content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping(
      value = "/{id}",
      consumes = {"multipart/form-data"})
  public ResponseEntity<CategoryResponseDTO> updateProductCategory(
      @PathVariable("id") Long id, @Valid @ModelAttribute CategoryRequestDTO dto) {
    return ResponseEntity.ok(productCategoryService.update(id, dto));
  }

  @Operation(
      summary = "Delete a product category",
      description = "Delete a product category by its ID.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully deleted product category",
      content = @Content(schema = @Schema(implementation = Void.class)))
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProductCategory(@PathVariable("id") Long id) {
    productCategoryService.delete(id);
    return ResponseEntity.ok().build();
  }
}
