package com.se114p12.backend.dtos.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

  @NotBlank private String fullname;

  @NotBlank
  @Pattern(
      regexp = "^(\\+84|0)(3[2-9]|5[2-9]|7[0|6-9]|8[1-9]|9[0-4|6-9])[0-9]{7}$",
      message = "Invalid phone number")
  private String phone;

  @Email private String email;

  @NotBlank private String username;

  @NotBlank private String password;
}
