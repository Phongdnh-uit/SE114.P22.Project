package com.se114p12.backend.services.authentication;

import com.se114p12.backend.entities.authentication.RefreshToken;

public interface RefreshTokenService {
  RefreshToken findByToken(String token);

  RefreshToken generateRefreshToken(Long userId);

  RefreshToken verifyExpiration(RefreshToken refreshToken);

  void deleteByToken(String token);
}
