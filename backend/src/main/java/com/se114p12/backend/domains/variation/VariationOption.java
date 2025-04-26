package com.se114p12.backend.domains.variation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "variation_options")
public class VariationOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull
    private String value;

    @NotNull
    private Double additionalPrice;

    @ManyToOne
    @JoinColumn(name = "variation_id", nullable = true)
    @JsonBackReference
    private Variation variation;
}