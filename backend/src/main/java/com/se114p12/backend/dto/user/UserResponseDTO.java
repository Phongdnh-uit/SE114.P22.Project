package com.se114p12.backend.dto.user;

import com.se114p12.backend.dto.BaseResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.general.NotificationUser;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseDTO extends BaseResponseDTO {
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private UserStatus status;
    private LoginProvider loginProvider;
    private Role role;
    private Cart cart;
    private List<NotificationUser> notifications = new ArrayList<>();
}