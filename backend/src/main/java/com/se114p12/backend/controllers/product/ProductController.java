package com.se114p12.backend.controllers.product;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.product.ProductRequestDTO;
import com.se114p12.backend.dtos.product.ProductResponseDTO;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.services.product.ProductService;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Module", description = "APIs for managing products")
@RestController
@RequestMapping(AppConstant.API_BASE_PATH + "/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @Operation(
      summary = "Get all products",
      description = "Retrieve a list of all products with optional filtering")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved products")})
  @ErrorResponse
  @GetMapping
  public ResponseEntity<PageVO<ProductResponseDTO>> getAllProducts(
      @ParameterObject Pageable pageable,
      @Filter @Parameter(name = "filter") Specification<Product> specification) {
    pageable = pageable.isPaged() ? pageable : Pageable.unpaged();
    return ResponseEntity.ok(productService.getAllProducts(specification, pageable));
  }

  @Operation(
      summary = "Get products by category",
      description = "Retrieve a list of products by category ID")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved products by category")
  @ErrorResponse
  @GetMapping("/category/{id}")
  public ResponseEntity<PageVO<ProductResponseDTO>> getProductsByCategory(
      @PathVariable("id") Long categoryId, @ParameterObject Pageable pageable) {
    return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
  }

  @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved product",
      content = @Content(schema = @Schema(implementation = ProductResponseDTO.class)))
  @ErrorResponse
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @Operation(summary = "Create a new product", description = "Create a new product entry")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully created product",
      content = @Content(schema = @Schema(implementation = ProductResponseDTO.class)))
  @ErrorResponse
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProductResponseDTO> createProduct(
      @Valid @ModelAttribute ProductRequestDTO productRequestDTO) {
    return ResponseEntity.ok(productService.create(productRequestDTO));
  }

  @Operation(summary = "Update a product", description = "Update an existing product entry")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully updated product",
      content = @Content(schema = @Schema(implementation = ProductResponseDTO.class)))
  @ErrorResponse
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProductResponseDTO> updateProduct(
      @PathVariable("id") Long id, @Valid @ModelAttribute ProductRequestDTO productRequestDTO) {
    return ResponseEntity.ok(productService.update(id, productRequestDTO));
  }

  @Operation(summary = "Delete a product", description = "Delete a product by its ID")
  @ApiResponse(responseCode = "204", description = "Successfully deleted product")
  @ErrorResponse
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get recommended products",
      description = "Retrieve a list of recommended products based on user preferences")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved recommended products",
      content = @Content(schema = @Schema(implementation = ProductResponseDTO.class)))
  @ErrorResponse
  @GetMapping("/recommended")
  public ResponseEntity<List<ProductResponseDTO>> getRecommendedProducts() {
    return ResponseEntity.ok(productService.getRecommendedProducts());
  }
}
