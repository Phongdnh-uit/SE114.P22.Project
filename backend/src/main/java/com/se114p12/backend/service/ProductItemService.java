package com.se114p12.backend.service;

import com.se114p12.backend.domain.ProductItem;
import com.se114p12.backend.repository.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;

    public List<ProductItem> getAllProductItems() {
        return productItemRepository.findAll();
    }

    public Optional<ProductItem> getProductItemById(Long id) {
        return productItemRepository.findById(id);
    }

    public ProductItem saveProductItem(ProductItem productItem) {
        return productItemRepository.save(productItem);
    }

    public void deleteProductItem(Long id) {
        productItemRepository.deleteById(id);
    }
}