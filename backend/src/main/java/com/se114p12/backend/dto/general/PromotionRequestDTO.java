package com.se114p12.backend.dto.general;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class PromotionRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Float discountAmount;

    @NotNull
    private Double minValue;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    private String code;
}