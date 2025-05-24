package com.se114p12.backend.services.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.dto.authentication.PasswordChangeDTO;
import com.se114p12.backend.dto.authentication.RegisterRequestDTO;
import com.se114p12.backend.dto.user.UserRequestDTO;
import com.se114p12.backend.dto.user.UserResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.enums.VerificationType;
import com.se114p12.backend.exception.BadRequestException;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.mapper.user.UserMapper;
import com.se114p12.backend.repository.authentication.RoleRepository;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final CartRepository cartRepository;
  private final JwtUtil jwtUtil;
  private final VerificationService verificationService;
  private final MailService mailService;
  private final SMSService smsService;

  @Override
  public PageVO<UserResponseDTO> getAllUsers(Specification<User> specification, Pageable pageable) {
    Page<User> userPage = userRepository.findAll(specification, pageable);
    List<UserResponseDTO> userResponseDTOs =
        userPage.getContent().stream().map(userMapper::entityToResponse).toList();
    return PageVO.<UserResponseDTO>builder()
        .page(userPage.getNumber())
        .size(userPage.getSize())
        .numberOfElements(userPage.getNumberOfElements())
        .totalPages(userPage.getTotalPages())
        .totalElements(userPage.getTotalElements())
        .content(userResponseDTOs)
        .build();
  }

  @Override
  public UserResponseDTO getUserById(Long id) {
    return userMapper.entityToResponse(findUserById(id));
  }

  @Override
  public UserResponseDTO findByPhone(String phone) {
    User user =
        userRepository
            .findByPhone(phone)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return userMapper.entityToResponse(user);
  }

  @Override
  public UserResponseDTO register(RegisterRequestDTO registerRequestDTO) {
    User user = new User();
    user.setFullname(registerRequestDTO.getFullname());
    user.setUsername(registerRequestDTO.getUsername());
    user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
    user.setEmail(registerRequestDTO.getEmail());
    if (!smsService.lookupPhoneNumber(registerRequestDTO.getPhone())) {
      throw new BadRequestException("Invalid phone number");
    }
    user.setPhone(SMSService.formatPhoneNumber(registerRequestDTO.getPhone()));
    user.setStatus(UserStatus.PENDING);
    user.setLoginProvider(LoginProvider.LOCAL);
    User savedUser = userRepository.save(user);

    Role userRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    user.setRole(userRole);
    // Tạo cart sau khi đăng ký
    Cart cart = new Cart();
    cart.setUser(savedUser);
    cartRepository.save(cart);

    // verify email
    Verification verification = verificationService.createActivationVerification(savedUser.getId());
    mailService.sendActivationEmail(user.getEmail(), verification.getCode());
    return userMapper.entityToResponse(savedUser);
  }

  @Override
  public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
    User user = findUserById(id);
    validateUserUniqueness(userRequestDTO, user);
    userMapper.partialUpdate(userRequestDTO, user);
    user = userRepository.save(user);
    return userMapper.entityToResponse(user);
  }

  @Override
  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found");
    }
    userRepository.deleteById(id);
  }

  @Override
  public void resetPassword(PasswordChangeDTO passwordChangeDTO) {
    Long userId = jwtUtil.getCurrentUserId();
    User currentUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    if (!passwordEncoder.matches(
        passwordChangeDTO.getCurrentPassword(), currentUser.getPassword())) {
      throw new BadRequestException("Password doesn't match");
    }
    currentUser.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
  }

  // Register or get user from Google
  @Override
  public UserResponseDTO getOrRegisterGoogleUser(GoogleIdToken.Payload payload) {
    Optional<User> userOptional = userRepository.findByEmail(payload.getEmail());
    if (userOptional.isPresent()) return userMapper.entityToResponse(userOptional.get());
    User user = new User();
    user.setEmail(payload.getEmail());
    user.setFullname(payload.get("name").toString());
    user.setUsername(payload.get("name").toString());
    user.setPassword("");
    user.setLoginProvider(LoginProvider.GOOGLE);
    user.setStatus(UserStatus.ACTIVE);
    user = userRepository.save(user);
    Role userRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    return userMapper.entityToResponse(user);
  }

  @Override
  public void verifyEmail(String code) {
    Verification verification =
        verificationService.verifyVerificationCode(code, VerificationType.ACTIVATION);
    User user = verification.getUser();
    user.setStatus(UserStatus.ACTIVE);
    userRepository.save(user);
    verificationService.deleteVerification(verification);
  }

  @Override
  public void assignRoleToUser(Long userId, Long roleId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    Role role =
        roleRepository
            .findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    user.setRole(role);
    userRepository.save(user);
  }

  @Override
  public void updateUserStatus(Long id, UserStatus status) {
    User user = findUserById(id);
    user.setStatus(status);
    userRepository.save(user);
  }

  // Private helper methods
  private User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private void validateUserUniqueness(UserRequestDTO userRequestDTO, User existingUser) {
    if ((existingUser == null || !userRequestDTO.getUsername().equals(existingUser.getUsername()))
        && userRepository.existsByUsername(userRequestDTO.getUsername())) {
      throw new DataConflictException("Username already exists");
    }

    if ((existingUser == null || !userRequestDTO.getEmail().equals(existingUser.getEmail()))
        && userRepository.existsByEmail(userRequestDTO.getEmail())) {
      throw new DataConflictException("Email already exists");
    }

    if ((existingUser == null || !userRequestDTO.getPhone().equals(existingUser.getPhone()))
        && userRepository.existsByPhone(userRequestDTO.getPhone())) {
      throw new DataConflictException("Phone already exists");
    }
  }

  @Override
  public UserResponseDTO getCurrentUser() {
    Long userId = jwtUtil.getCurrentUserId();
    if (userId == null) {
      throw new ResourceNotFoundException("User not authenticated");
    }
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return userMapper.entityToResponse(user);
  }
}
