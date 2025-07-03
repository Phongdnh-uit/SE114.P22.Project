package com.se114p12.backend.dtos.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendVerifyEmailRequestDTO {

  @NotBlank @Email private String email;
}
