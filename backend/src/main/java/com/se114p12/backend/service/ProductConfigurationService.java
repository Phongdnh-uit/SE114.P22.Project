package com.se114p12.backend.service;

import com.se114p12.backend.domain.ProductConfiguration;
import com.se114p12.backend.domain.ProductItem;
import com.se114p12.backend.domain.VariationOption;
import com.se114p12.backend.repository.ProductConfigurationRepository;
import com.se114p12.backend.repository.ProductItemRepository;
import com.se114p12.backend.repository.VariationOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductConfigurationService {

    private final ProductConfigurationRepository productConfigurationRepository;
    private final ProductItemRepository productItemRepository;
    private final VariationOptionRepository variationOptionRepository;

    public ProductConfiguration createProductConfiguration(Long productItemId, Long variationOptionId) {
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new RuntimeException("ProductItem not found"));

        VariationOption variationOption = variationOptionRepository.findById(variationOptionId)
                .orElseThrow(() -> new RuntimeException("VariationOption not found"));

        ProductConfiguration config = new ProductConfiguration();
        config.setProductItem(productItem);
        config.setVariationOption(variationOption);

        return productConfigurationRepository.save(config);
    }

    public List<ProductConfiguration> getAllConfigurations() {
        return productConfigurationRepository.findAll();
    }
}