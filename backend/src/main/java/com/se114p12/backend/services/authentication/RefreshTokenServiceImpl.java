package com.se114p12.backend.services.authentication;

import com.se114p12.backend.entities.authentication.RefreshToken;
import com.se114p12.backend.exception.RefreshTokenException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.RefreshTokenRepository;
import com.se114p12.backend.repository.authentication.UserRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  @Value("${jwt.refresh.token.expiration}")
  private Long refreshTokenExpirationTime;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  @Override
  public RefreshToken findByToken(String token) {
    return refreshTokenRepository
        .findByToken(token)
        .orElseThrow(() -> new RefreshTokenException("Invalid token"));
  }

  @Override
  public RefreshToken generateRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setExpiredAt(Instant.now().plusSeconds(refreshTokenExpirationTime));
    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken refreshToken) {
    if (refreshToken.getExpiredAt().isBefore(Instant.now())) {
      refreshTokenRepository.delete(refreshToken);
      throw new RefreshTokenException("Refresh token was expired");
    }
    return refreshToken;
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void cleanExpiredRefreshTokens() {
    refreshTokenRepository.deleteExpiredTokens();
  }

  @Override
  public void deleteByToken(String token) {
    refreshTokenRepository.deleteByToken(token);
  }
}
