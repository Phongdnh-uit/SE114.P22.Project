package com.se114p12.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_item_id", nullable = false)
    @JsonBackReference
    private ProductItem productItem;

    @ManyToOne
    @JoinColumn(name = "variation_option_id", nullable = false)
    @JsonBackReference
    private VariationOption variationOption;
}