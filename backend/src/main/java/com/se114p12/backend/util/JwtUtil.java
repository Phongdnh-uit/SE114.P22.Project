package com.se114p12.backend.util;

import com.se114p12.backend.domain.authentication.User;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUtil {
  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
  private final LoginUtil loginUtil;
  private final UserRepository userRepository;

  @Value("${jwt.access.token.expiration}")
  private Long accessTokenExpirationTime;

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  public String generateAccessToken(Long userId) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Instant now = Instant.now();
    Instant exp = now.plusSeconds(accessTokenExpirationTime);
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(user.getId().toString())
            .issuedAt(now)
            .claim("role", user.getRole().getName())
            .expiresAt(exp)
            .build();
    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  /**
   * Extracts the principal (user identifier) from the Authentication object. Supports UserDetails
   * (userId), Jwt (sub).
   *
   * @param authentication the Authentication object from SecurityContext
   * @return the user identifier (preferably userId)
   * @throws IllegalStateException if principal type is unsupported
   */
  private Long extractPrincipal(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails userDetails) {
      return loginUtil.getUserByCredentialId(userDetails.getUsername()).getId();
    } else if (principal instanceof Jwt jwt) {
      return Long.parseLong(jwt.getSubject());
    }
    throw new IllegalStateException(
        "Unsupported principal type: " + principal.getClass().getName());
  }

  public Long getCurrentUserId() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("No authenticated user found");
    }
    return extractPrincipal(authentication);
  }

  public Jwt checkValidity(String token) {
    try {
      return this.jwtDecoder.decode(token);
    } catch (JwtException e) {
      throw new JwtException("Invalid JWT token", e);
    }
  }
}
