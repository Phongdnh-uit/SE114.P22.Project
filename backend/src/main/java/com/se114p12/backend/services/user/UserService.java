package com.se114p12.backend.services.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.domains.authentication.Verification;
import com.se114p12.backend.domains.cart.Cart;
import com.se114p12.backend.dto.request.PasswordChangeDTO;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.enums.VerificationType;
import com.se114p12.backend.exception.BadRequestException;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.repository.cart.CartRepository;
import com.se114p12.backend.services.authentication.VerificationService;
import com.se114p12.backend.services.general.MailService;
import com.se114p12.backend.services.general.SMSService;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
