package com.se114p12.backend.dto.user;

import com.se114p12.backend.dto.BaseResponseDTO;
import com.se114p12.backend.dto.role.RoleResponseDTO;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private RoleResponseDTO role;
//    private Cart cart;
//    private List<NotificationUser> notifications = new ArrayList<>();
}