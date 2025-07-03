package com.se114p12.backend.dtos.variation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VariationOptionRequestDTO {
    @NotNull
    private String value;

    @NotNull
    private Double additionalPrice;

    @NotNull
    private Long variationId;
}