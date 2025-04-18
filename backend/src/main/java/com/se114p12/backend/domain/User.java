package com.se114p12.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.se114p12.backend.domain.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String fullname;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+\\d*$", message = "Username must contains letters and numbers and must begin with a letter")
    @Column(unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotBlank
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private String avatarUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonIgnore
    private String refreshToken;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
