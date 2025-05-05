package com.se114p12.backend.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private String variationInfo;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Long orderId;
}
