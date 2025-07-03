package com.se114p12.backend.entities.product;

import com.se114p12.backend.entities.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "product_categories")
@Data
public class ProductCategory extends BaseEntity {

  @Column(unique = true)
  private String name;

  private String description;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products = new ArrayList<>();

  @Column(length = 500)
  private String imageUrl;
}
