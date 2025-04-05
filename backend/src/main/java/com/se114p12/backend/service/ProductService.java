package com.se114p12.backend.service;

import com.se114p12.backend.domain.Product;
import com.se114p12.backend.domain.ProductCategory;
import com.se114p12.backend.repository.ProductRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    public ProductService(ProductRepository productRepository, ProductCategoryService productCategoryService) {
        this.productRepository = productRepository;
        this.productCategoryService = productCategoryService;
    }

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

    public Product create(Product product) {
        product.setProductId(null);
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category ID is required.");
        }

        ProductCategory category = productCategoryService.findById(product.getCategory().getId());
        product.setCategory(category);

        product.setCreatedAt(Instant.now());
        product.setIsAvailable(true);
        return productRepository.save(product);
    }


    public Product update(Long id, @NotNull Product product) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with the given ID does not exist.");
        }
        product.setUpdatedAt(Instant.now());
        product.setProductId(id); // Ensure the ID is consistent with the path parameter.
        return productRepository.save(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with the given ID does not exist.");
        }
        productRepository.deleteById(id);
    }

    private boolean isDuplicateProduct(Product product) {
        return productRepository.existsByName(product.getName()) ||
                productRepository.existsByShortDescription(product.getShortDescription());
    }
}