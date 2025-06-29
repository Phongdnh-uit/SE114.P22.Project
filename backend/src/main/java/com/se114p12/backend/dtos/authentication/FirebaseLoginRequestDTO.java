package com.se114p12.backend.dtos.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseLoginRequestDTO {
  @NotNull private String idToken;
}
