package com.se114p12.backend.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequestDTO {

  @NotBlank private String fullname;

  @NotBlank
  @Pattern(
      regexp = "^[a-zA-Z][a-zA-Z0-9]*$",
      message = "Username must contains letters and numbers and must begin with a letter")
  private String username;

  @NotNull
  @Email(message = "Invalid email format")
  private String email;

  @Pattern(
      regexp = "^(\\+84|0)(3[2-9]|5[2-9]|7[0|6-9]|8[1-9]|9[0-4|6-9])[0-9]{7}$",
      message = "Invalid phone number")
  private String phone;

  private MultipartFile avatar;

  @NotNull(message = "role id cannot be null")
  private Long roleId;
}
