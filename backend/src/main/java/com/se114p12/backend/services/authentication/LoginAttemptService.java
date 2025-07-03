package com.se114p12.backend.services.authentication;

public interface LoginAttemptService {

  public static final int MAX_ATTEMPTS = 5;
  public static final long BLOCK_TIME = 5 * 60 * 1000; // 50 minutes in milliseconds
  public static final String KEY_PREFIX = "login_attempts:";

  void loginSucceeded(Long userId);

  void loginFailed(Long userId);

  boolean isBlocked(Long userId);
}
