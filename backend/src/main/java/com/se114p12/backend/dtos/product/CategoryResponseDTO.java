package com.se114p12.backend.dtos.product;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponseDTO extends BaseResponseDTO {
    private String name;
    private String description;
    private String imageUrl;
}
