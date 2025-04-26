package com.se114p12.backend.domains.order;

import com.se114p12.backend.domains.BaseEntity;
import com.se114p12.backend.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order extends BaseEntity {
  @NotNull private Instant orderDate;

  @NotNull private String shippingAddress;

  private Long totalPrice;

  @NotNull private Instant expectedDeliveryTime;
  @NotNull private Instant actualDeliveryTime;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
