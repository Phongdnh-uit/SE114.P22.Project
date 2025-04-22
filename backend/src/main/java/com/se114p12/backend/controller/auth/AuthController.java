package com.se114p12.backend.controller.auth;

import com.se114p12.backend.dto.request.LoginRequestDTO;
import com.se114p12.backend.dto.request.PasswordChangeDTO;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.dto.response.AuthResponseDTO;
import com.se114p12.backend.service.UserService;
import com.se114p12.backend.service.mail.MailService;
import com.se114p12.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Module")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtUtil jwtUtil;
  private final MailService mailService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
    return ResponseEntity.ok().body(userService.register(registerRequestDTO));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(
      @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.getCredentialId(), loginRequestDTO.getPassword());

    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Long userId = jwtUtil.getCurrentUserId();

    String accessToken = jwtUtil.generateAccessToken(userId);

    String refreshToken = jwtUtil.generateRefreshToken(userId);

    userService.setUserRefreshToken(userId, refreshToken);
    AuthResponseDTO loginResponseDTO = new AuthResponseDTO();
    loginResponseDTO.setAccessToken(accessToken);
    loginResponseDTO.setRefreshToken(refreshToken);
    return ResponseEntity.ok().body(loginResponseDTO);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody String refreshToken) {
    Jwt jwt = jwtUtil.checkValidity(refreshToken);
    Long userId = Long.parseLong(jwt.getSubject());

    String accessToken = jwtUtil.generateAccessToken(userId);

    String newRefreshToken = jwtUtil.generateRefreshToken(userId);

    userService.setUserRefreshToken(userId, refreshToken);

    AuthResponseDTO loginResponseDTO = new AuthResponseDTO();
    loginResponseDTO.setAccessToken(accessToken);
    loginResponseDTO.setRefreshToken(newRefreshToken);
    return ResponseEntity.ok().body(loginResponseDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout() {
    Long userId = jwtUtil.getCurrentUserId();
    userService.setUserRefreshToken(userId, null);
    return ResponseEntity.ok().body("Logout successfully");
  }

  @PostMapping("/change-password")
  public ResponseEntity<String> changePassword(
      @Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
    userService.resetPassword(passwordChangeDTO);
    return ResponseEntity.ok().body("Reset password successfully");
  }

  @PostMapping("/reset-password/init")
  public void requestPasswordReset() {}

  @PostMapping("/reset-password/finish")
  public void finishPasswordReset() {}
}
