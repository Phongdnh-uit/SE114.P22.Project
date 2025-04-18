package com.se114p12.backend.domain.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.se114p12.backend.domain.variation.Variation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Variation> variations;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductItem> productItems;

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
    private BigDecimal originalPrice;

    @Column(length = 500)
    private String imageUrl;

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