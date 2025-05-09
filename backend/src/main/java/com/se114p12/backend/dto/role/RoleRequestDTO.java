package com.se114p12.backend.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDTO {
    @NotBlank
    private String name;
    private String description;
    private Boolean active;
}
