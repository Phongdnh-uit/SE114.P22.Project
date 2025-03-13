package com.se114p12.backend.controller;

import com.se114p12.backend.dto.request.LoginRequestDTO;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.service.UserService;
import com.se114p12.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok().body(userService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()
        );


        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateAccessToken(loginRequestDTO.getUsername());

        String refreshToken = jwtUtil.generateRefreshToken(loginRequestDTO.getUsername());

        // save refresh token to database ??

        return ResponseEntity.ok().body(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
             @RequestBody String refreshToken) {
        Jwt jwt = jwtUtil.checkValidity(refreshToken);
        String email = jwt.getSubject();

        String accessToken = jwtUtil.generateAccessToken(email);

        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        // save new refresh token to database ??

        return ResponseEntity.ok().body(Map.of(
                "accessToken", accessToken,
                "refreshToken", newRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken) {
        // delete refresh token from database ??
        return ResponseEntity.ok().body("Logout successfully");
    }

}
