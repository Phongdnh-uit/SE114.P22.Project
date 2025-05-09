package com.se114p12.backend.entities.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Table(name = "order_details")
@Data
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private Long productId;
  private String productName;
  private String productImage;
  private String variationInfo;

  @NotNull private Long quantity;

  // Giá của một món
  @NotNull private BigDecimal price;
}
