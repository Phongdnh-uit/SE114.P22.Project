package com.se114p12.backend.dto.promotion;

import com.se114p12.backend.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class PromotionResponseDTO extends BaseResponseDTO {
    private String description;
    private Float discountAmount;
    private Double minValue;
    private Instant startDate;
    private Instant endDate;
    private String code;
}