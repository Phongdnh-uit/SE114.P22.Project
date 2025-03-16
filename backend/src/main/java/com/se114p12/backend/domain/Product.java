package com.se114p12.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String name;

    @Column(length = 500)
    @NotBlank
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String detailDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotBlank
    private BigDecimal originalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Column(nullable = false)
    private Boolean isAvailable;
}