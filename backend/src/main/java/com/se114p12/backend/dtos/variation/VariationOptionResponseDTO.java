package com.se114p12.backend.dtos.variation;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;

@Data
public class VariationOptionResponseDTO extends BaseResponseDTO {
    private String value;
    private Double additionalPrice;
    private Long variationId;
}