package com.se114p12.backend.dtos.cart;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartItemRequestDTO {
    private Long productId;
    private Long quantity;
    private Set<Long> variationOptionIds;
}
