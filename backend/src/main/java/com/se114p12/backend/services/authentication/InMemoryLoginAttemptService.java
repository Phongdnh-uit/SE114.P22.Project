package com.se114p12.backend.services.authentication;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class InMemoryLoginAttemptService implements LoginAttemptService {
  private final Cache<Long, Integer> attempts = Caffeine.newBuilder()
      .expireAfterWrite(5, TimeUnit.MINUTES)
      .maximumSize(10_000)
      .build();

  public void loginSucceeded(Long userId) {
    attempts.invalidate(userId);
  }

  public void loginFailed(Long userId) {
    Integer count = attempts.getIfPresent(userId);
    if (count == null) count = 0;
    attempts.put(userId, count + 1);
  }

  public boolean isBlocked(Long userId) {
    Integer count = attempts.getIfPresent(userId);
    return count != null && count >= 5;
  }
}
