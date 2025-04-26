package com.se114p12.backend.dto.request.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequestDTO {
    private long categoryId;

    private List<Long> variationIds;

    private String name;

    private String shortDescription;

    private String detailDescription;

    private BigDecimal originalPrice;

    private MultipartFile image;
}
