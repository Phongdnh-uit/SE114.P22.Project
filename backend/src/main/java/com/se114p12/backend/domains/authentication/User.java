package com.se114p12.backend.domains.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.se114p12.backend.domains.BaseEntity;
import com.se114p12.backend.domains.cart.Cart;
import com.se114p12.backend.domains.general.Notification;
import com.se114p12.backend.domains.general.NotificationUser;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank
    private String fullname;

    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z]+\\d*$",
            message = "Username must contains letters and numbers and must begin with a letter")
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

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationUser> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Verification> verifications = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<Review> reviews = new ArrayList<>();
}
