package com.se114p12.backend.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank
    private String fullname;

    @NotBlank
    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Invalid phone number")
    private String phone;

    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
