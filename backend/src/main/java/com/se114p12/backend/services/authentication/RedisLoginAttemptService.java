package com.se114p12.backend.services.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisLoginAttemptService implements LoginAttemptService {

  private final RedisTemplate<String, Object> redisTemplate;

  public void loginSucceeded(Long userId) {
    redisTemplate.delete(KEY_PREFIX + userId);
  }

  public void loginFailed(Long userId) {
    Integer attempts = (Integer) redisTemplate.opsForValue().get(KEY_PREFIX + userId);
    if (attempts == null) {
      attempts = 0;
    }
    redisTemplate.opsForValue().set(KEY_PREFIX + userId, attempts++, BLOCK_TIME);
  }

  public boolean isBlocked(Long userId) {
    Integer attempts = (Integer) redisTemplate.opsForValue().get(KEY_PREFIX + userId);
    return attempts != null && attempts >= MAX_ATTEMPTS;
  }
}
