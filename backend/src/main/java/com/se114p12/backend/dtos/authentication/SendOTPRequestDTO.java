package com.se114p12.backend.dtos.authentication;

import com.se114p12.backend.enums.OTPAction;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendOTPRequestDTO {
  @NotBlank @Email private String email;
  @NotNull private OTPAction action;
}
