package com.se114p12.backend.dto.promotion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class PromotionRequestDTO {

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