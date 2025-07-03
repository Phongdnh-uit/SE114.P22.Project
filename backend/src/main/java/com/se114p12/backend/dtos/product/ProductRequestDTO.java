package com.se114p12.backend.dtos.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequestDTO {
    private long categoryId;

    private String name;

    private String shortDescription;

    private String detailDescription;

    private BigDecimal originalPrice;

    private MultipartFile image;
}
