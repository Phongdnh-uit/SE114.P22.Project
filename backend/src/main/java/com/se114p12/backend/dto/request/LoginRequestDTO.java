package com.se114p12.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    // credentialId can be username or email or phone number
    @NotBlank
    private String credentialId;

    @NotBlank
    private String password;
}
