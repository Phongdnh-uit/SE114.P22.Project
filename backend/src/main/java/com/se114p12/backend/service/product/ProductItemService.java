package com.se114p12.backend.service.product;

import com.se114p12.backend.domain.product.Product;
import com.se114p12.backend.domain.product.ProductItem;
import com.se114p12.backend.domain.variation.VariationOption;
import com.se114p12.backend.dto.request.ProductItemRequestDTO;
import com.se114p12.backend.repository.product.ProductItemRepository;
import com.se114p12.backend.repository.product.ProductRepository;
import com.se114p12.backend.repository.variation.VariationOptionRepository;
import com.se114p12.backend.service.storage.StorageService;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductItemService {

    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final VariationOptionRepository variationOptionRepository;
    private final StorageService storageService;

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

    public ProductItem createProductItem(ProductItemRequestDTO dto) {
        // Lấy sản phẩm
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Lấy danh sách VariationOption
        List<VariationOption> variationOptions = variationOptionRepository.findAllById(dto.getVariationOptionIds());

        // Kiểm tra không trùng loại Variation
        Map<Long, VariationOption> uniqueVariations = new HashMap<>();
        for (VariationOption option : variationOptions) {
            if (option.getVariation() != null) {
                Long variationId = option.getVariation().getId();
                if (uniqueVariations.containsKey(variationId)) {
                    throw new IllegalArgumentException("Không thể có nhiều VariationOption cùng một loại Variation.");
                }
                uniqueVariations.put(variationId, option);
            }
        }

        // Tạo ProductItem mới
        ProductItem productItem = new ProductItem();
        productItem.setProduct(product);
        productItem.setPrice(dto.getPrice());
        productItem.setVariationOptions(new ArrayList<>(uniqueVariations.values()));

        return productItemRepository.save(productItem);
    }

    public void deleteProductItem(Long id) {
        productItemRepository.deleteById(id);
    }
}