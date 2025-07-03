package com.se114p12.backend.util;

import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginType;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.repositories.authentication.UserRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUtil {

  // Phone number start with 03/05/07/08/09 (VN)
  public static final Pattern PHONE_PATTERN = Pattern.compile("^(03|05|07|08|09)[0-9]{8}$");

  public static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  // Username can contains 30 chars with letters,
  // numbers, or "_", and must start with a letter
  private static final Pattern USERNAME_PATTERN =
      Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_-]{2,29}$");

  private final UserRepository userRepository;

  public static LoginType checkCredentialIdType(String credentialId) {
    if (credentialId == null || credentialId.isEmpty())
      throw new IllegalArgumentException("Credential ID cannot be null or empty");

    // normalize input
    String normalizedInput = normalizeInput(credentialId);

    // check phone
    if (PHONE_PATTERN.matcher(normalizedInput).matches()) {
      return LoginType.PHONE;
    }

    // check email
    if (EMAIL_PATTERN.matcher(normalizedInput).matches()) {
      return LoginType.EMAIL;
    }

    // Check username
    if (USERNAME_PATTERN.matcher(normalizedInput).matches()) {
      return LoginType.USERNAME;
    }

    throw new IllegalArgumentException(
        "Invalid credentialId format: not in phone, email or username format");
  }

  public User getUserByCredentialId(String credentialId) {
    LoginType loginType = checkCredentialIdType(credentialId);
    // Normalize the input to ensure consistent matching
    credentialId = normalizeInput(credentialId);
    if (loginType == LoginType.PHONE) {
      if (credentialId.startsWith("0")) {
        credentialId = credentialId.substring(1);
        credentialId = "+84" + credentialId;
      }
    }

    Optional<User> userOptional =
        switch (loginType) {
          case PHONE -> userRepository.findByPhone(credentialId);
          case EMAIL -> userRepository.findByEmail(credentialId);
          case USERNAME -> userRepository.findByUsername(credentialId);
          default -> Optional.empty();
        };
    return userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private static String normalizeInput(String input) {
    String normalized = input.trim();
    if (normalized.contains("@")) {
      normalized = normalized.toLowerCase();
    } else if (normalized.matches(".*\\d.*")) {
      normalized = normalized.replaceAll("[\\s-]", "");
    }
    return normalized;
  }
}
