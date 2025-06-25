package com.se114p12.backend.dtos.review;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ReviewRequestDTO {
    @NotNull
    private Long orderId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rate;

    @Nullable
    private String content;

    @Nullable
    private String reply;
}