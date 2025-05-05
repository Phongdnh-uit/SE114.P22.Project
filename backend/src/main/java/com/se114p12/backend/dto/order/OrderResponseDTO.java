package com.se114p12.backend.dto.order;

import com.se114p12.backend.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private String shippingAddress;
    private BigDecimal totalPrice;
    private String note;
    private Instant expectedDeliveryTime;
    private Instant actualDeliveryTime;
    private OrderStatus orderStatus;
    private Long userId;
    private List<OrderDetailResponseDTO> orderDetails;
    private Instant createdAt;
    private Instant updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
