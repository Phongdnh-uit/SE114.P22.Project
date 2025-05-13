package com.se114p12.backend.services.product;

import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.dto.product.ProductRequestDTO;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.product.ProductCategoryRepository;
import com.se114p12.backend.repository.product.ProductRepository;
import com.se114p12.backend.services.general.StorageService;
import com.se114p12.backend.vo.PageVO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final StorageService storageService;

    public PageVO<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategory_Id(categoryId, pageable);
        return PageVO.<Product>builder()
                .content(productPage.getContent())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .numberOfElements(productPage.getNumberOfElements())
                .build();
    }

    public Product create(ProductRequestDTO dto) {
        ProductCategory category = productCategoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found.")
        );
        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        Product product = Product.builder()
                .category(category)
                .name(dto.getName())
                .shortDescription(dto.getShortDescription())
                .detailDescription(dto.getDetailDescription())
                .originalPrice(dto.getOriginalPrice())
                .isAvailable(true)
                .build();

        product.setImageUrl(storageService.store(dto.getImage(), "product-items"));

        return productRepository.save(product);
    }

    public Product update(Long id, @NotNull ProductRequestDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with the given ID does not exist."));

        existingProduct.setName(dto.getName());
        existingProduct.setShortDescription(dto.getShortDescription());
        existingProduct.setDetailDescription(dto.getDetailDescription());
        existingProduct.setOriginalPrice(dto.getOriginalPrice());
        existingProduct.setUpdatedAt(Instant.now());

        if (dto.getCategoryId() != 0) {
            ProductCategory category = productCategoryRepository.findById(dto.getCategoryId()).orElseThrow(
                    () -> new ResourceNotFoundException("Category not found.")
            );
            existingProduct.setCategory(category);
        }

        existingProduct.setImageUrl(storageService.store(dto.getImage(), "product-items"));

        return productRepository.save(existingProduct);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

//    private String processImage(MultipartFile image, String folder) {
//        return (image != null && !image.isEmpty()) ? "/uploads/" + folder + "/" + storageService.store(image, folder) : null;
//    }
}