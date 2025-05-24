package com.se114p12.backend.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.se114p12.backend.dto.authentication.GoogleLoginRequestDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.exception.BadRequestException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.services.authentication.CustomUserDetails;
import java.time.Instant;
import java.util.Collections;
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
      return ((CustomUserDetails) userDetails).getId();
    } else if (principal instanceof Jwt jwt) {
      return Long.parseLong(jwt.getSubject());
    } else if (principal instanceof String) {
      // fallback case: 'anonymous' user
      return null;
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

  public GoogleIdToken verifyGoogleCredential(GoogleLoginRequestDTO googleLoginRequest) {
    try {
      GoogleIdTokenVerifier verifier =
          new GoogleIdTokenVerifier.Builder(
                  new NetHttpTransport(), GsonFactory.getDefaultInstance())
              .setAudience(Collections.singletonList(googleLoginRequest.getClientId()))
              .build();

      GoogleIdToken googleIdToken = verifier.verify(googleLoginRequest.getCredential());
      if (googleIdToken == null) {
        throw new BadRequestException("Invalid Google ID token");
      }
      return googleIdToken;
    } catch (Exception e) {
      throw new BadRequestException(e.getMessage());
    }
  }
}
