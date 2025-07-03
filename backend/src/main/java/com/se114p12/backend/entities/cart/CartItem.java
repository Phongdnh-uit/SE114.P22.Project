package com.se114p12.backend.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.variation.VariationOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "cart_id", nullable = false)
  @JsonIgnore
  private Cart cart;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @NotNull
  private Long quantity;

  @NotNull
  private BigDecimal price;

  @ManyToMany
  @JoinTable(
      name = "cart_item_variation_options",
      joinColumns = @JoinColumn(name = "cart_item_id"),
      inverseJoinColumns = @JoinColumn(name = "variation_option_id"))
  private Set<VariationOption> variationOptions = new HashSet<>();

  private Boolean available = true;
}
