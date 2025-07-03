package com.se114p12.backend.dtos.promotion;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class PromotionRequestDTO {

    private String description;

    @NotNull
    private BigDecimal discountValue;

    @NotNull
    private Double minValue;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    private String code;

    private Boolean isPublic;

    @Nullable
    private List<Long> userIds;
}