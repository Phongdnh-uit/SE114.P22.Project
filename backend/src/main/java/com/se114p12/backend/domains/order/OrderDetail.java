package com.se114p12.backend.domains.order;

import com.se114p12.backend.domains.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

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

  private String productId;
  private String productName;
  private String productImage;
  private String variationInfo;

  @NotNull
  private Long quantity;

  // Giá của một món
  @NotNull
  private BigDecimal originalPrice;

  // Giá sau khi nhân với số lượng
  @NotNull
  private BigDecimal price;
}