package com.se114p12.backend.services.authentication;

import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.enums.VerificationType;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.repository.authentication.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private static final Long ACTIVATION_EXPIRATION_TIME = 60 * 60L; // 1 hour
    private static final Long RESET_PASSWORD_EXPIRATION_TIME = 15 * 60L; // 15 minutes

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;

    @Override
    public Verification createActivationVerification(Long userId) {
        return createVerification(userId, VerificationType.ACTIVATION, ACTIVATION_EXPIRATION_TIME);
    }

    @Override
    public Verification createResetPasswordVerification(Long userId) {
        return createVerification(userId, VerificationType.RESET_PASSWORD, RESET_PASSWORD_EXPIRATION_TIME);
    }

    @Override
    public Verification verifyVerificationCode(String code, VerificationType type) {
        Verification verification = verificationRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Verification not found"));
        if (!verification.getType().equals(type)) {
            throw new ResourceNotFoundException("Verification type mismatch");
        }
        if (verification.getExpiredAt().isBefore(Instant.now())) {
            throw new ResourceNotFoundException("Verification code expired");
        }
        return verification;
    }

    @Override
    public void deleteVerification(Verification verification) {
        verificationRepository.delete(verification);
    }

    private Verification createVerification(Long userId, VerificationType type, Long expirationTime) {
        Verification verification = new Verification();
        verification.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found")));
        verification.setType(type);
        verification.setCode(UUID.randomUUID().toString());
        verification.setExpiredAt(Instant.now().plusSeconds(expirationTime));
        return verificationRepository.save(verification);
    }
}
