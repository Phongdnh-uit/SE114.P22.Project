package com.se114p12.backend.controllers.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.se114p12.backend.dto.user.UserResponseDTO;
import com.se114p12.backend.entities.authentication.RefreshToken;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.dto.authentication.*;
import com.se114p12.backend.enums.OTPAction;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.enums.VerificationType;
import com.se114p12.backend.exception.BadRequestException;
import com.se114p12.backend.services.user.UserService;
import com.se114p12.backend.services.authentication.RefreshTokenService;
import com.se114p12.backend.services.authentication.VerificationService;
import com.se114p12.backend.services.general.MailService;
import com.se114p12.backend.services.general.SMSService;
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

import java.util.Map;

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
    private final SMSService smsService;
    private final VerificationService verificationService;

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
        UserResponseDTO user = userService.getOrRegisterGoogleUser(payload);

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

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String code) {
        userService.verifyEmail(code);
        return ResponseEntity.ok().body("Verify email successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        userService.resetPassword(passwordChangeDTO);
        return ResponseEntity.ok().body("Reset password successfully");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@Valid @RequestBody SendOTPRequestDTO sendOTPRequestDTO) {
        smsService.sendOtp(sendOTPRequestDTO.getPhoneNumber());
        return ResponseEntity.ok().body("Send OTP successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOTPRequestDTO verifyOTPRequestDTO) {
        boolean isValid =
                smsService.verifyOtp(
                        verifyOTPRequestDTO.getPhoneNumber(), verifyOTPRequestDTO.getOtp());
        if (!isValid) {
            throw new BadRequestException("Invalid OTP");
        }
        UserResponseDTO user = userService.findByPhone(verifyOTPRequestDTO.getPhoneNumber());
        if (verifyOTPRequestDTO.getAction() == OTPAction.VERIFY_PHONE) {
            userService.updateUserStatus(user.getId(), UserStatus.ACTIVE);
            return ResponseEntity.ok().body("Verify phone successfully");
        } else if (verifyOTPRequestDTO.getAction() == OTPAction.FORGOT_PASSWORD) {
            Verification verification = verificationService.createResetPasswordVerification(user.getId());
            return ResponseEntity.ok().body(Map.of("code", verification.getCode()));
        }
        throw new BadRequestException("Invalid action");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        Verification verification = verificationService.verifyVerificationCode(
                forgotPasswordRequestDTO.getCode(), VerificationType.RESET_PASSWORD);
        User user = verification.getUser();
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setCurrentPassword(user.getPassword());
        passwordChangeDTO.setNewPassword(forgotPasswordRequestDTO.getNewPassword());
        userService.resetPassword(passwordChangeDTO);
        return ResponseEntity.ok().body("Send reset password email successfully");
    }
}
