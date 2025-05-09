package com.se114p12.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank
    private String fullname;

    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z]+\\d*$",
            message = "Username must contains letters and numbers and must begin with a letter")
    private String username;

    @NotNull
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Invalid phone number")
    private String phone;

    private String avatarUrl;

    @NotNull(message = "role id cannot be null")
    private Long roleId;
}
