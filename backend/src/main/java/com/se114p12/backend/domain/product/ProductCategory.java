package com.se114p12.backend.domain.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Data
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private String description;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

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
