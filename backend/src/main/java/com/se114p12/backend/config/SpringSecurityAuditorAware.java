package com.se114p12.backend.config;

import com.se114p12.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SpringSecurityAuditorAware implements AuditorAware<Long> {
    private final JwtUtil jwtUtil;

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(jwtUtil.getCurrentUserId());
    }
}
