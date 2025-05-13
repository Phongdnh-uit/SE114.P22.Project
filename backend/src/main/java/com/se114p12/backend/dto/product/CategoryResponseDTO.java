package com.se114p12.backend.dto.product;

import com.se114p12.backend.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponseDTO extends BaseResponseDTO {
    private String name;
    private String description;
}
