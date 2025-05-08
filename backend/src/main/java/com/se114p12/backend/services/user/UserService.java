package com.se114p12.backend.services.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.entities.authentication.User;
import com.se114p12.backend.dto.authentication.PasswordChangeDTO;
import com.se114p12.backend.dto.authentication.RegisterRequestDTO;
import com.se114p12.backend.vo.PageVO;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface UserService {

    User findByPhone(String phone);

    User update(Long id, User user);

    void delete(Long id);

    PageVO<User> getAllUsers(Pageable pageable);

    User getUserById(Long id);

    List<User> searchUsers(String keyword);

    User register(RegisterRequestDTO registerRequestDTO);

    void resetPassword(PasswordChangeDTO passwordChangeDTO);

    User getOrRegisterGoogleUser(GoogleIdToken.Payload payload);

    void verifyEmail(String code);

    void assignRoleToUser(Long userId, Long roleId);
}
