package com.se114p12.backend.controllers.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.domains.authentication.RefreshToken;
import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.dto.request.GoogleLoginRequestDTO;
import com.se114p12.backend.dto.request.LoginRequestDTO;
import com.se114p12.backend.dto.request.PasswordChangeDTO;
import com.se114p12.backend.dto.request.RefreshTokenRequestDTO;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.dto.response.AuthResponseDTO;
import com.se114p12.backend.services.UserService;
import com.se114p12.backend.services.authentication.RefreshTokenService;
import com.se114p12.backend.services.mail.MailService;
import com.se114p12.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Module")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtUtil jwtUtil;
  private final RefreshTokenService refreshTokenService;
  private final MailService mailService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
    return ResponseEntity.ok().body(userService.register(registerRequestDTO));
  }

  @PostMapping("/oauth2/google")
  public ResponseEntity<AuthResponseDTO> loginWithGoogle(
      @Valid @RequestBody GoogleLoginRequestDTO googleLoginRequest) {

     GoogleIdToken googleIdToken = jwtUtil.verifyGoogleCredential(googleLoginRequest);
     GoogleIdToken.Payload payload = googleIdToken.getPayload();

//     get user or register user if not exists
     User user = userService.getOrRegisterGoogleUser(payload);

//     create access token
     String accessToken = jwtUtil.generateAccessToken(user.getId());

     // create refresh token
     String refreshToken = refreshTokenService.generateRefreshToken(user.getId()).getToken();

     AuthResponseDTO authenticationResponseDTO = new AuthResponseDTO();
     authenticationResponseDTO.setAccessToken(accessToken);
     authenticationResponseDTO.setRefreshToken(refreshToken);
     return ResponseEntity.ok(authenticationResponseDTO);
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

    String refreshToken = refreshTokenService.generateRefreshToken(userId).getToken();

    AuthResponseDTO loginResponseDTO = new AuthResponseDTO();
    loginResponseDTO.setAccessToken(accessToken);
    loginResponseDTO.setRefreshToken(refreshToken);
    return ResponseEntity.ok().body(loginResponseDTO);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponseDTO> refreshToken(
      @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    RefreshToken refreshToken =
        refreshTokenService.verifyExpiration(
            refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken()));

    String accessToken = jwtUtil.generateAccessToken(refreshToken.getUser().getId());

    AuthResponseDTO loginResponseDTO = new AuthResponseDTO();
    loginResponseDTO.setAccessToken(accessToken);
    loginResponseDTO.setRefreshToken(refreshTokenRequestDTO.getRefreshToken());
    return ResponseEntity.ok().body(loginResponseDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(
      @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    refreshTokenService.deleteByToken(refreshTokenRequestDTO.getRefreshToken());
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
