package com.se114p12.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.se114p12.backend.domain.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

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
    @Column(unique = true)
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String refreshToken;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
