package com.se114p12.backend.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
  private String accessToken;
  private String refreshToken;
}
