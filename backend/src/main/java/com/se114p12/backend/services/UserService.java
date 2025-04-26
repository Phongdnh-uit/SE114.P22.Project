package com.se114p12.backend.services;

import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.domains.cart.Cart;
import com.se114p12.backend.dto.request.PasswordChangeDTO;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.exception.BadRequestException;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.repository.cart.CartRepository;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final JwtUtil jwtUtil;

  //    public User create(User user) {
  //        user.setId(null);
  //        validateUserUniqueness(user, null);
  //        user.setPassword(passwordEncoder.encode(user.getPassword()));
  //        user.setStatus(UserStatus.PENDING);
  //
  //        User createdUser = userRepository.save(user);
  //
  //        // Tạo cart cho user mới
  //        cartService.createForUser(createdUser.getId());
  //
  //        return createdUser;
  //    }

  public User update(Long id, User user) {
    User existingUser = findUserById(id);
    validateUserUniqueness(user, existingUser);
    updateUserDetails(existingUser, user);
    return userRepository.save(existingUser);
  }

  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found");
    }
    userRepository.deleteById(id);
  }

  public PageVO<User> getAllUsers(Pageable pageable) {
    Page<User> userPage = userRepository.findAll(pageable);
    return PageVO.<User>builder()
        .page(userPage.getNumber())
        .size(userPage.getSize())
        .numberOfElements(userPage.getNumberOfElements())
        .totalPages(userPage.getTotalPages())
        .totalElements(userPage.getTotalElements())
        .content(userPage.getContent())
        .build();
  }

  public User getUserById(Long id) {
    return findUserById(id);
  }

  public List<User> searchUsers(String keyword) {
    return userRepository.findUserByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        keyword, keyword);
  }

  public User register(RegisterRequestDTO registerRequestDTO) {
    User user = new User();
    user.setFullname(registerRequestDTO.getFullname());
    user.setUsername(registerRequestDTO.getUsername());
    user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
    user.setEmail(registerRequestDTO.getEmail());
    user.setPhone(registerRequestDTO.getPhone());
    user.setStatus(UserStatus.PENDING);
    User savedUser = userRepository.save(user);

    // Tạo cart sau khi đăng ký
    Cart cart = new Cart();
    cart.setUser(savedUser);
    cartRepository.save(cart);

    return savedUser;
  }

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

  // Private helper methods

  private User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private void validateUserUniqueness(User user, User existingUser) {
    if ((existingUser == null || !user.getUsername().equals(existingUser.getUsername()))
        && userRepository.existsByUsername(user.getUsername())) {
      throw new DataConflictException("Username already exists");
    }

    if ((existingUser == null || !user.getEmail().equals(existingUser.getEmail()))
        && userRepository.existsByEmail(user.getEmail())) {
      throw new DataConflictException("Email already exists");
    }

    if ((existingUser == null || !user.getPhone().equals(existingUser.getPhone()))
        && userRepository.existsByPhone(user.getPhone())) {
      throw new DataConflictException("Phone already exists");
    }
  }

  private void updateUserDetails(User existingUser, User newUser) {
    existingUser.setFullname(newUser.getFullname());
    existingUser.setUsername(newUser.getUsername());
    existingUser.setEmail(newUser.getEmail());
    existingUser.setPhone(newUser.getPhone());
    existingUser.setAvatarUrl(newUser.getAvatarUrl());
    existingUser.setStatus(newUser.getStatus());
  }
}
