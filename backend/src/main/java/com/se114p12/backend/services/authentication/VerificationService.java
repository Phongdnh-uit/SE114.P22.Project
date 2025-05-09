package com.se114p12.backend.services.authentication;

import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.enums.VerificationType;

public interface VerificationService {
    Verification createActivationVerification(Long userId);

    Verification createResetPasswordVerification(Long userId);

    Verification verifyVerificationCode(String code, VerificationType type);

    void deleteVerification(Verification verification);
}
