package com.se114p12.backend.dtos.variation;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;

@Data
public class VariationResponseDTO extends BaseResponseDTO {
    private String name;
    private Boolean isMultipleChoice;
    private Long productId;
}
