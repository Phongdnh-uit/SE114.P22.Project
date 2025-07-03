package com.se114p12.backend.dtos.variation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VariationRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private Boolean isMultipleChoice;

    @NotNull
    private Long productId;
}
