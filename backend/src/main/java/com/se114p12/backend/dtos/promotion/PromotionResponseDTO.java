package com.se114p12.backend.dtos.promotion;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class PromotionResponseDTO extends BaseResponseDTO {
    private String description;
    private BigDecimal discountValue;
    private Double minValue;
    private Instant startDate;
    private Instant endDate;
    private String code;
    private Boolean isPublic;
}