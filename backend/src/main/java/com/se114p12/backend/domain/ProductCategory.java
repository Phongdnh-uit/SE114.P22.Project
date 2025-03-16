package com.se114p12.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se114p12.backend.util.JwtUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "product_categories")
@Data
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;
    private String description;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

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
