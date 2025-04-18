package com.se114p12.backend.controller.auth;

import com.se114p12.backend.dto.request.LoginRequestDTO;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.service.mail.MailService;
import com.se114p12.backend.service.UserService;
import com.se114p12.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
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
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String accessToken = jwtUtil.generateAccessToken(loginRequestDTO.getUsername());

    String refreshToken = jwtUtil.generateRefreshToken(loginRequestDTO.getUsername());

    userService.setUserRefreshToken(loginRequestDTO.getUsername(), refreshToken);

    return ResponseEntity.ok()
        .body(
            Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken));
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
    Jwt jwt = jwtUtil.checkValidity(refreshToken);
    String phone = jwt.getSubject();

    String accessToken = jwtUtil.generateAccessToken(phone);

    String newRefreshToken = jwtUtil.generateRefreshToken(phone);

    userService.setUserRefreshToken(phone, refreshToken);

    return ResponseEntity.ok()
        .body(
            Map.of(
                "accessToken", accessToken,
                "refreshToken", newRefreshToken));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    String phone = JwtUtil.getCurrentUserCredentials();

    userService.setUserRefreshToken(phone, null);

    return ResponseEntity.ok().body("Logout successfully");
  }

/**
 * On working
 * */
    
  // @PostMapping("/forgot-password")
  // public void forgotPassword(Model model) {
  //   model.addAttribute("name", "Phong");
  //   try {
  //     mailService.sendEmail(
  //         "dangnguyenhuyphong@gmail.com", "Forgot password", "forgot-password", model);
  //   } catch (MessagingException e) {
  //     e.printStackTrace();
  //   }
  // }

  // @PostMapping("/reset-password")

}
