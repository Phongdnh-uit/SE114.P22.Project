package com.se114p12.backend.services.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.dto.user.UserRequestDTO;
import com.se114p12.backend.dto.user.UserResponseDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.dto.authentication.PasswordChangeDTO;
import com.se114p12.backend.dto.authentication.RegisterRequestDTO;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.vo.PageVO;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    PageVO<UserResponseDTO> getAllUsers(Specification<User> specification, Pageable pageable);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO findByPhone(String phone);

    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);

    UserResponseDTO getOrRegisterGoogleUser(GoogleIdToken.Payload payload);

    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);

    void delete(Long id);

    void resetPassword(PasswordChangeDTO passwordChangeDTO);

    void verifyEmail(String code);

    void assignRoleToUser(Long userId, Long roleId);

    void updateUserStatus(Long id, UserStatus status);
}
