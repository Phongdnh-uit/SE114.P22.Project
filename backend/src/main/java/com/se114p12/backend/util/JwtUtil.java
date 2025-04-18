package com.se114p12.backend.util;

import com.se114p12.backend.domain.authentication.User;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {
  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
  private final UserRepository userRepository;

  @Value("${jwt.access.token.expiration}")
  private Long accessTokenExpirationTime;

  @Value("${jwt.refresh.token.expiration}")
  private Long refreshTokenExpirationTime;

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  public JwtUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserRepository userRepository) {
    this.jwtEncoder = jwtEncoder;
    this.jwtDecoder = jwtDecoder;
    this.userRepository = userRepository;
  }

  public String generateAccessToken(String credential) {

    User user;

    if (TypeUtil.checkUsernameType(credential) == 1) {
      user = userRepository.findByPhone(credential).orElseThrow();
    } else if (TypeUtil.checkUsernameType(credential) == 2) {
      user = userRepository.findByEmail(credential).orElseThrow();
    } else {
      user = userRepository.findByUsername(credential).orElseThrow();
    }
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(accessTokenExpirationTime);
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(credential)
            .issuedAt(now)
            .claim("role", user.getRole().getName())
            .expiresAt(exp)
            .build();
    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  public String generateRefreshToken(String credential) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(refreshTokenExpirationTime);
    JwtClaimsSet claims =
        JwtClaimsSet.builder().subject(credential).issuedAt(now).expiresAt(exp).build();
    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  private static String extractPrincipal(Authentication authentication) {
    if (authentication == null) {
      return null;
    } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
      return springSecurityUser.getUsername();
    } else if (authentication.getPrincipal() instanceof Jwt jwt) {
      return jwt.getSubject();
    } else if (authentication.getPrincipal() instanceof String s) {
      return s;
    }
    return null;
  }

  public static String getCurrentUserCredentials() {
    SecurityContext context = SecurityContextHolder.getContext();
    return Optional.ofNullable(extractPrincipal(context.getAuthentication()))
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  public Jwt checkValidity(String token) {
    try {
      return this.jwtDecoder.decode(token);
    } catch (JwtException e) {
      throw new JwtException("Invalid JWT token", e);
    }
  }

  public Long getCurrentUserId() {
    String credential = getCurrentUserCredentials();

    // Tuỳ vào hệ thống dùng username/email/phone:
    Optional<User> userOptional;

    if (TypeUtil.checkUsernameType(credential) == 1) {
      userOptional = userRepository.findByPhone(credential);
    } else if (TypeUtil.checkUsernameType(credential) == 2) {
      userOptional = userRepository.findByEmail(credential);
    } else {
      userOptional = userRepository.findByUsername(credential);
    }

    return userOptional
            .orElseThrow(() -> new ResourceNotFoundException("User not found"))
            .getId();
  }
}
