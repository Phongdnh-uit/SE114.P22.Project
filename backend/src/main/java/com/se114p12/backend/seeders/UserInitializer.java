package com.se114p12.backend.seeders;

import com.se114p12.backend.dtos.authentication.RegisterRequestDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.repositories.authentication.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInitializer {
  private final UserRepository userRepository;
  private final Validator validator;
  private final PasswordEncoder passwordEncoder;

  // Admin user details
  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.password}")
  private String adminPassword;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.phone}")
  private String adminPhone;

  public void initializeAdminUser(Role adminRole) {

    if (adminRole.getName() == null || !adminRole.getName().equals(RoleName.ADMIN.getValue())) {
      throw new IllegalArgumentException("Invalid admin role provided");
    }

    if (!adminRole.getUsers().isEmpty()) {
      System.out.println("Admin user already exists, skipping initialization.");
      return;
    }

    // Validate admin user information
    validateAdminInfo();

    // Check if the admin user already exists
    User adminUser = new User();
    adminUser.setRole(adminRole);
    adminUser.setUsername(adminUsername);
    adminUser.setPhone(adminPhone);
    adminUser.setEmail(adminEmail);
    adminUser.setPassword(passwordEncoder.encode(adminPassword));
    adminUser.setFullname(adminUsername);
    adminUser.setLoginProvider(LoginProvider.LOCAL);
    adminUser.setStatus(UserStatus.ACTIVE);
    userRepository.save(adminUser);
  }

  private void validateAdminInfo() {
    // use spring validation annotations in RegisterRequestDTO class
    RegisterRequestDTO request = new RegisterRequestDTO();
    request.setFullname(adminUsername);
    request.setUsername(adminUsername);
    request.setEmail(adminEmail);
    request.setPhone(adminPhone);
    request.setPassword(adminPassword);

    Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder("Invalid admin user information: ");
      for (ConstraintViolation<RegisterRequestDTO> violation : violations) {
        errorMessage
            .append(violation.getPropertyPath())
            .append(" ")
            .append(violation.getMessage())
            .append("; ");
      }
      throw new IllegalArgumentException(errorMessage.toString());
    }

    if (userRepository.existsByUsername(adminUsername)) {
      throw new IllegalArgumentException("Username already exists: " + adminUsername);
    }

    if (userRepository.existsByEmail(adminEmail)) {
      throw new IllegalArgumentException("Email already exists: " + adminEmail);
    }

    if (userRepository.existsByPhone(adminPhone)) {
      throw new IllegalArgumentException("Phone number already exists: " + adminPhone);
    }
  }
}
