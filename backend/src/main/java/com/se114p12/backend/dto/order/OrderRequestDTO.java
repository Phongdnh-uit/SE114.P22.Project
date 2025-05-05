package com.se114p12.backend.dto.order;

import com.se114p12.backend.domains.cart.CartItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class OrderRequestDTO {
    @NotNull
    private String shippingAddress;
    private String note;
    @NotNull
    private Long userId;
    private Set<Long> cartItemIds = new HashSet<>();
}
