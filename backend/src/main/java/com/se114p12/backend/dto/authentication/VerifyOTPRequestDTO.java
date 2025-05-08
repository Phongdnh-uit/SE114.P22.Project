package com.se114p12.backend.dto.authentication;

import com.se114p12.backend.enums.OTPAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyOTPRequestDTO {
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String otp;
    @NotNull
    private OTPAction action;
}
