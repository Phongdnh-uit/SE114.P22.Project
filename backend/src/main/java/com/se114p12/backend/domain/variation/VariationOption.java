package com.se114p12.backend.domain.variation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se114p12.backend.domain.product.ProductItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "variation_options")
public class VariationOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String value;

    @NotNull
    private double additionalPrice;

    @ManyToOne
    @JoinColumn(name = "variation_id", nullable = true)
    @JsonBackReference
    private Variation variation;

    @ManyToMany(mappedBy = "variationOptions")
    @JsonIgnore
    private List<ProductItem> productItems;
}
