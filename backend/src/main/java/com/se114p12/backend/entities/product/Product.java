package com.se114p12.backend.entities.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.variation.Variation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Variation> variations;

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
}