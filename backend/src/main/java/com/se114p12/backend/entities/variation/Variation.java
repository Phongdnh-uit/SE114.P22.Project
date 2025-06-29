package com.se114p12.backend.entities.variation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "variations")
public class Variation extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = true)
  @JsonBackReference
  private Product product;

  @Column(nullable = false, length = 255)
  @NotBlank
  private String name;

  @OneToMany(mappedBy = "variation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VariationOption> variationOptions = new ArrayList<>();

  private Boolean isMultipleChoice;
}
