package com.se114p12.backend.mappers.cart;

import com.se114p12.backend.dtos.cart.CartItemRequestDTO;
import com.se114p12.backend.dtos.cart.CartItemResponseDTO;
import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.variation.Variation;
import com.se114p12.backend.entities.variation.VariationOption;
import com.se114p12.backend.mappers.product.ProductMapper;
import com.se114p12.backend.mappers.variation.VariationOptionMapper;
import com.se114p12.backend.repositories.product.ProductRepository;
import com.se114p12.backend.repositories.variation.VariationOptionRepository;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, VariationOptionMapper.class})
public interface CartItemMapper {

    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProduct")
    @Mapping(target = "variationOptions", source = "variationOptionIds", qualifiedByName = "mapVariationOptions")
    CartItem toEntity(CartItemRequestDTO dto,
                      @Context ProductRepository productRepo,
                      @Context VariationOptionRepository variationRepo);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "variationOptions", target = "variationOptionIds", qualifiedByName = "mapVariationOptionIds")
    CartItemResponseDTO toDTO(CartItem entity);

    @AfterMapping
    default void enrichDTO(CartItem entity, @MappingTarget CartItemResponseDTO dto) {
        if (entity.getProduct() != null) {
            dto.setProductName(entity.getProduct().getName());
            dto.setImageUrl(entity.getProduct().getImageUrl());
            dto.setPrice(entity.getPrice());
        }

        if (entity.getVariationOptions() != null && !entity.getVariationOptions().isEmpty()) {
            String variationNames = entity.getVariationOptions().stream()
                    .filter(vo -> vo.getVariation() != null)
                    .collect(Collectors.groupingBy(VariationOption::getVariation)) // Nhóm theo Variation
                    .entrySet().stream()
                    .sorted(Comparator.comparing(entry -> entry.getKey().getId()))
                    .map(entry -> {
                        Variation variation = entry.getKey();
                        String variationName = variation.getName();
                        String values = entry.getValue().stream()
                                .map(VariationOption::getValue)
                                .collect(Collectors.joining(", "));

                        return variationName + ": " + values;
                    })
                    .collect(Collectors.joining(", "));

            dto.setVariationOptionInfo(variationNames);
        }
    }


    @Named("mapProduct")
    static Product mapProduct(Long productId, @Context ProductRepository productRepo) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại: " + productId));
    }

    @Named("mapVariationOptions")
    static Set<VariationOption> mapVariationOptions(Set<Long> ids, @Context VariationOptionRepository repo) {
        return ids.stream()
                .map(id -> repo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Tùy chọn biến thể không tồn tại: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("mapVariationOptionIds")
    static Set<Long> mapVariationOptionIds(Set<VariationOption> options) {
        return options.stream()
                .map(VariationOption::getId)
                .collect(Collectors.toSet());
    }
}