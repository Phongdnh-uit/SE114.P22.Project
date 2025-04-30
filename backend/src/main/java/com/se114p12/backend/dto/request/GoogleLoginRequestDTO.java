package com.se114p12.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequestDTO {
  @NotBlank private String clientId;

  @NotBlank private String credential;
}
