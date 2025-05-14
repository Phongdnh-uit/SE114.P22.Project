package com.se114p12.backend.services.product;

import com.se114p12.backend.dto.product.ProductRequestDTO;
import com.se114p12.backend.dto.product.ProductResponseDTO;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.mapper.product.ProductMapper;
import com.se114p12.backend.repository.product.ProductCategoryRepository;
import com.se114p12.backend.repository.product.ProductRepository;
import com.se114p12.backend.services.general.StorageService;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final StorageService storageService;
    private final ProductMapper productMapper;

    @Override
    public PageVO<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategory_Id(categoryId, pageable);
        List<ProductResponseDTO> productDTOs = productPage
                .map(productMapper::entityToResponse)
                .getContent();

        return PageVO.<ProductResponseDTO>builder()
                .content(productDTOs)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .numberOfElements(productPage.getNumberOfElements())
                .build();
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        Product product = Product.builder()
                .category(category)
                .name(dto.getName())
                .shortDescription(dto.getShortDescription())
                .detailDescription(dto.getDetailDescription())
                .originalPrice(dto.getOriginalPrice())
                .isAvailable(true)
                .build();

        product.setImageUrl(storageService.store(dto.getImage(), "product-items"));
        product = productRepository.save(product);

        return productMapper.entityToResponse(product);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existingProduct.setName(dto.getName());
        existingProduct.setShortDescription(dto.getShortDescription());
        existingProduct.setDetailDescription(dto.getDetailDescription());
        existingProduct.setOriginalPrice(dto.getOriginalPrice());
        existingProduct.setUpdatedAt(Instant.now());

        if (dto.getCategoryId() != 0) {
            ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
            existingProduct.setCategory(category);
        }

        existingProduct.setImageUrl(storageService.store(dto.getImage(), "product-items"));
        existingProduct = productRepository.save(existingProduct);

        return productMapper.entityToResponse(existingProduct);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
}