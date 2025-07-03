package com.se114p12.backend.dtos.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOTPResponseDTO {
  private String code;
}
