package com.se114p12.backend.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se114p12.backend.enums.ErrorType;
import com.se114p12.backend.services.authentication.LoginAttemptService;
import com.se114p12.backend.util.LoginUtil;
import com.se114p12.backend.vo.ErrorVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;
  private final LoginAttemptService loginAttemptService;
  private final LoginUtil loginUtil;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    if (authException != null) {
      if (authException instanceof BadCredentialsException) {
        String credentialId = request.getParameter("credentialId");
        Long userId = loginUtil.getUserByCredentialId(credentialId).getId();
        loginAttemptService.loginFailed(userId);
      }
      ErrorVO errorVO =
          ErrorVO.builder()
              .type(ErrorType.AUTHENTICATION_ERROR)
              .details(Map.of("message", authException.getMessage()))
              .build();

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      objectMapper.writeValue(response.getWriter(), errorVO);
    } else {
      String credentialId = request.getParameter("credentialId");
      Long userId = loginUtil.getUserByCredentialId(credentialId).getId();
      loginAttemptService.loginSucceeded(userId);
    }
  }
}
