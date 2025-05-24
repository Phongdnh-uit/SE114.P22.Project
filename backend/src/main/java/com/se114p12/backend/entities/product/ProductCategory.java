package com.se114p12.backend.entities.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.se114p12.backend.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "product_categories")
@Data
public class ProductCategory extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> product;

    @Column(length = 500)
    private String imageUrl;
}
