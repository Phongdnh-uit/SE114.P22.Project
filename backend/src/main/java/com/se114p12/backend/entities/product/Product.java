package com.se114p12.backend.entities.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.entities.variation.Variation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@SQLRestriction("deleted = false")
public class Product extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = true)
  @JsonBackReference
  private ProductCategory category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Variation> variations = new ArrayList<>();

  @Column(nullable = false, length = 255)
  @NotBlank
  private String name;

  @Column(length = 500)
  private String shortDescription;

  @Column(columnDefinition = "TEXT")
  private String detailDescription;

  @Column(nullable = false, precision = 10, scale = 2)
  @NotNull
  private BigDecimal originalPrice;

  //    @Column(precision = 10, scale = 2)
  //    private BigDecimal discountPrice;

  @Column(length = 500)
  private String imageUrl;

  @Column(nullable = false)
  @NotNull
  private Boolean isAvailable;

  private Boolean deleted = false;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItem> cartItems = new ArrayList<>();
}
