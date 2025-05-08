package com.se114p12.backend.repository.authentication;

import com.se114p12.backend.entities.authentication.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository
    extends JpaRepository<RefreshToken, Long>, JpaSpecificationExecutor<RefreshToken> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  @Query("DELETE FROM RefreshToken r WHERE r.expiredAt < CURRENT_TIMESTAMP")
  void deleteExpiredTokens();

  void deleteByToken(String token);
}
