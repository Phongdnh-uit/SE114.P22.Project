package com.se114p12.backend.dtos.authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseRegisterRequestDTO {
  @NotNull private String idToken;

  @NotNull
  @Pattern(
      regexp = "^(\\+84|0)(3[2-9]|5[2-9]|7[0|6-9]|8[1-9]|9[0-4|6-9])[0-9]{7}$",
      message = "Invalid phone number")
  private String phoneNumber;
}
