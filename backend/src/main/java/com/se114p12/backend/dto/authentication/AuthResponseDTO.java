package com.se114p12.backend.dto.authentication;

import lombok.Data;

@Data
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
}
