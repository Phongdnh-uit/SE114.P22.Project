package com.se114p12.backend.domain;

import com.se114p12.backend.domain.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Instant orderDate;

  @NotNull private String shippingAddress;

  private Long totalPrice;

  @NotNull private Instant expectedDeliveryTime;
  @NotNull private Instant actualDeliveryTime;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
