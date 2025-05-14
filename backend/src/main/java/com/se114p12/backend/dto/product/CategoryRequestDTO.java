package com.se114p12.backend.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryRequestDTO {
    @NotBlank
    private String name;

    private String description;

    private MultipartFile image;
}
