package com.se114p12.backend.dtos.order;

import com.se114p12.backend.dtos.BaseResponseDTO;
import com.se114p12.backend.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.se114p12.backend.enums.PaymentMethod;
import com.se114p12.backend.enums.PaymentStatus;
import lombok.Data;

@Data
public class OrderResponseDTO extends BaseResponseDTO {
  private Double destinationLatitude;
  private Double destinationLongitude;
  private String shippingAddress;
  private BigDecimal totalPrice;
  private String note;
  private Instant expectedDeliveryTime;
  private Instant actualDeliveryTime;
  private OrderStatus orderStatus;
  private Long userId;
  private Long shipperId;
  private PaymentMethod paymentMethod;
  private PaymentStatus paymentStatus;
  private List<OrderDetailResponseDTO> orderDetails;
}
