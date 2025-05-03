package com.se114p12.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOTPRequestDTO {
    @NotBlank
    private String phoneNumber;
}
