package com.se114p12.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank
    private String fullname;

    @NotBlank
    private String phone;

    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
