package com.se114p12.backend.config;

import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AppInterceptor implements HandlerInterceptor {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    Long userId = jwtUtil.getCurrentUserId();
    if (userId != null) {
      User user =
          userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
      if (user.getStatus() != UserStatus.ACTIVE) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not active");
        return false;
      }
    }
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
