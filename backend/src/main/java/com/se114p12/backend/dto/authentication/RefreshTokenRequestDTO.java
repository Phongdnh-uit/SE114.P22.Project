package com.se114p12.backend.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
  @NotBlank private String refreshToken;
}
