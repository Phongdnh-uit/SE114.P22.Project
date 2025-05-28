package com.se114p12.backend.services.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.dtos.authentication.RegisterRequestDTO;
import com.se114p12.backend.dtos.user.UserRequestDTO;
import com.se114p12.backend.dtos.user.UserResponseDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
  PageVO<UserResponseDTO> getAllUsers(Specification<User> specification, Pageable pageable);

  UserResponseDTO getUserById(Long id);

  UserResponseDTO findByPhone(String phone);

  UserResponseDTO getOrRegisterGoogleUser(GoogleIdToken.Payload payload);

  UserResponseDTO register(RegisterRequestDTO userRequestDTO);

  UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);

  void delete(Long id);

  void assignRoleToUser(Long userId, Long roleId);

  void updateUserStatus(Long id, UserStatus status);

  UserResponseDTO getCurrentUser();
}
