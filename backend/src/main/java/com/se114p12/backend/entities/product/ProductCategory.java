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
    @NotBlank
    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Product> product;

//    @PrePersist
//    public void prePersist() {
//        createdAt = Instant.now();
//        createdBy = JwtUtil.getCurrentUserCredentials();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        updatedAt = Instant.now();
//        updatedBy = JwtUtil.getCurrentUserCredentials();
//    }

}
