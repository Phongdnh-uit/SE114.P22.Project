package com.se114p12.backend.dtos.authentication;

import com.se114p12.backend.dtos.user.UserResponseDTO;
import lombok.Data;

@Data
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
  private UserResponseDTO user;
}
