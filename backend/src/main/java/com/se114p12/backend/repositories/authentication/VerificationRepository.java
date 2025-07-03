package com.se114p12.backend.repositories.authentication;

import com.se114p12.backend.entities.authentication.Verification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository
    extends JpaRepository<Verification, Long>, JpaSpecificationExecutor<Verification> {
  Optional<Verification> findByCode(String code);

  @Modifying
  @Query("DELETE FROM Verification v WHERE v.expiredAt < CURRENT_TIMESTAMP")
  void deleteExpiredVerifications();
}
