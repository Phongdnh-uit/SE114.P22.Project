package com.se114p12.backend.service;

import com.se114p12.backend.domain.ProductItem;
import com.se114p12.backend.domain.VariationOption;
import com.se114p12.backend.repository.ProductItemRepository;
import com.se114p12.backend.repository.VariationOptionRepository;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final VariationOptionRepository variationOptionRepository;

    public PageVO<ProductItem> getAllProductItems(Pageable pageable) {
        Page<ProductItem> productItemPage = productItemRepository.findAll(pageable);
        return PageVO.<ProductItem>builder()
                .content(productItemPage.getContent())
                .page(productItemPage.getNumber())
                .size(productItemPage.getSize())
                .totalElements(productItemPage.getTotalElements())
                .totalPages(productItemPage.getTotalPages())
                .numberOfElements(productItemPage.getNumberOfElements())
                .build();
    }

    public Optional<ProductItem> getProductItemById(Long id) {
        return productItemRepository.findById(id);
    }

    public ProductItem saveProductItem(ProductItem productItem) {
        return productItemRepository.save(productItem);
    }

    public ProductItem createProductItem(ProductItem productItem, List<Long> variationOptionIds) {
        // Lấy danh sách VariationOption từ DB
        List<VariationOption> variationOptions = variationOptionRepository.findAllById(variationOptionIds);

        Map<Long, VariationOption> uniqueVariations = new HashMap<>();
        for (VariationOption option : variationOptions) {
            if (option.getVariation() != null) {
                Long variationId = option.getVariation().getId();
                if (uniqueVariations.containsKey(variationId)) {
                    throw new IllegalArgumentException("Khong the co nhieu VariationOption cung mot loai Variation.");
                }
                uniqueVariations.put(variationId, option);
            }
        }

        productItem.setVariationOptions(new ArrayList<>(uniqueVariations.values()));
        return productItemRepository.save(productItem);
    }

    public void deleteProductItem(Long id) {
        productItemRepository.deleteById(id);
    }
}