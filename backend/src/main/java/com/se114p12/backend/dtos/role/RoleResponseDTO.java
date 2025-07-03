package com.se114p12.backend.dtos.role;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleResponseDTO extends BaseResponseDTO {
    private String name;
    private String description;
    private Boolean active;
}
