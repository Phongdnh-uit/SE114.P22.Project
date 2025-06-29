package com.se114p12.backend.services.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.authentication.FirebaseRegisterRequestDTO;
import com.se114p12.backend.dtos.authentication.GoogleRegisterRequestDTO;
import com.se114p12.backend.dtos.authentication.RegisterRequestDTO;
import com.se114p12.backend.dtos.user.UserRequestDTO;
import com.se114p12.backend.dtos.user.UserResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.exceptions.BadRequestException;
import com.se114p12.backend.exceptions.DataConflictException;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.user.UserMapper;
import com.se114p12.backend.repositories.authentication.RoleRepository;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.services.authentication.VerificationService;
import com.se114p12.backend.services.general.MailService;
import com.se114p12.backend.services.general.SMSService;
import com.se114p12.backend.services.general.StorageService;
import com.se114p12.backend.util.ImageLoader;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.util.RandomUtil;
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
  private final JwtUtil jwtUtil;
  private final VerificationService verificationService;
  private final MailService mailService;
  private final SMSService smsService;
  private final StorageService storageService;
  private final ImageLoader imageLoader;

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
    if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
      throw new DataConflictException("Username already exists");
    }
    if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
      throw new DataConflictException("Email already exists");
    }
    if (userRepository.existsByPhone(registerRequestDTO.getPhone())) {
      throw new DataConflictException("Phone already exists");
    }
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
    if (userRequestDTO.getAvatar() != null && !userRequestDTO.getAvatar().isEmpty()) {
      String fileUri = storageService.store(userRequestDTO.getAvatar(), AppConstant.USER_FOLDER);
      user.setAvatarUrl(fileUri);
    }
    user = userRepository.save(user);
    return userMapper.entityToResponse(user);
  }

  @Override
  public void delete(Long id) {
    User user = findUserById(id);
    userRepository.delete(user);
    if (user.getAvatarUrl() != null && user.getAvatarUrl() != "") {
      storageService.delete(user.getAvatarUrl());
    }
  }

  // Register or get user from Google
  @Override
  public UserResponseDTO registerGoogleUser(GoogleRegisterRequestDTO googleRegisterRequestDTO) {
    GoogleIdToken googleIdToken =
        jwtUtil.verifyGoogleCredential(
            googleRegisterRequestDTO.getCredential(), googleRegisterRequestDTO.getClientId());
    GoogleIdToken.Payload payload = googleIdToken.getPayload();

    Optional<User> userOptional = userRepository.findByEmail(payload.getEmail());

    // If user exists, return the user response
    if (userOptional.isPresent()) return userMapper.entityToResponse(userOptional.get());

    if (userRepository.existsByPhone(
        SMSService.formatPhoneNumber(googleRegisterRequestDTO.getPhone()))) {
      throw new DataConflictException("Phone number already exists");
    }

    User user = new User();
    user.setEmail(payload.getEmail());
    user.setFullname(payload.get("name").toString());
    do {
      user.setUsername("user" + RandomUtil.generateRandomString(10));
    } while (userRepository.existsByUsername(user.getUsername()));
    if (!smsService.lookupPhoneNumber(googleRegisterRequestDTO.getPhone())) {
      throw new BadRequestException("Invalid phone number");
    }
    user.setPhone(SMSService.formatPhoneNumber(googleRegisterRequestDTO.getPhone()));
    user.setPassword("");
    user.setLoginProvider(LoginProvider.GOOGLE);
    user.setStatus(UserStatus.ACTIVE);
    String avatarUrl =
        imageLoader.saveImageFromUrl(payload.get("picture").toString(), AppConstant.USER_FOLDER);
    user.setAvatarUrl(avatarUrl);
    Role userRole =
        roleRepository
            .findByName(RoleName.USER.getValue())
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    user.setRole(userRole);
    user = userRepository.save(user);
    return userMapper.entityToResponse(user);
  }

  // Register or get user from Firebase
  @Override
  public UserResponseDTO registerFirebaseUser(
      FirebaseRegisterRequestDTO firebaseRegisterRequestDTO) {
    FirebaseToken payload;
    try {
      payload = FirebaseAuth.getInstance().verifyIdToken(firebaseRegisterRequestDTO.getIdToken());
    } catch (FirebaseAuthException e) {
      throw new BadRequestException("Invalid Firebase ID token");
    }

    Optional<User> userOptional = userRepository.findByEmail(payload.getEmail());

    // If user exists, return the user response
    if (userOptional.isPresent()) return userMapper.entityToResponse(userOptional.get());

    if (userRepository.existsByPhone(
        SMSService.formatPhoneNumber(firebaseRegisterRequestDTO.getPhoneNumber()))) {
      throw new DataConflictException("Phone number already exists");
    }

    User user = new User();
    user.setEmail(payload.getEmail());
    user.setFullname(payload.getName());
    do {
      user.setUsername("user" + RandomUtil.generateRandomString(10));
    } while (userRepository.existsByUsername(user.getUsername()));
    if (!smsService.lookupPhoneNumber(firebaseRegisterRequestDTO.getPhoneNumber())) {
      throw new BadRequestException("Invalid phone number");
    }
    user.setPhone(SMSService.formatPhoneNumber(firebaseRegisterRequestDTO.getPhoneNumber()));
    user.setPassword("");
    user.setLoginProvider(LoginProvider.FIREBASE);
    user.setStatus(UserStatus.ACTIVE);
    String avatarUrl = imageLoader.saveImageFromUrl(payload.getPicture(), AppConstant.USER_FOLDER);
    user.setAvatarUrl(avatarUrl);
    Role userRole =
        roleRepository
            .findByName(RoleName.USER.getValue())
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    user.setRole(userRole);
    user = userRepository.save(user);
    return userMapper.entityToResponse(user);
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
