package com.se114p12.backend.repository.authentication;

import com.se114p12.backend.entities.authentication.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>,
        JpaSpecificationExecutor<Verification> {
    Optional<Verification> findByCode(String code);
}
