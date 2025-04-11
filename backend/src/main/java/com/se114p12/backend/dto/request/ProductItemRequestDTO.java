package com.se114p12.backend.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductItemRequestDTO {
    private Long productId;

    private BigDecimal price;

    private MultipartFile image;

    private List<Long> variationOptionIds;
}