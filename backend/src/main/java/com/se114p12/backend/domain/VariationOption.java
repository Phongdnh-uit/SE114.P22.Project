package com.se114p12.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private double additionalPrice;

    @ManyToOne
    @JoinColumn(name = "variation_id", nullable = true)
    @JsonBackReference
    private Variation variation;

    @OneToMany(mappedBy = "variationOption", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductConfiguration> productConfigurations;
}
