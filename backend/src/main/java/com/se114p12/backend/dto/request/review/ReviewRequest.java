package com.se114p12.backend.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ReviewRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1) @Max(5)
    private Integer rate;

    @NotBlank
    private String content;

    @Nullable
    private String reply;
}
