package com.se114p12.backend.util;

import com.se114p12.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class JwtUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    @Value("${jwt.access.token.expiration}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenExpirationTime;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtUtil(JwtEncoder jwtEncoder,
                   JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateAccessToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessTokenExpirationTime);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(now)
                .expiresAt(exp)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String generateRefreshToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshTokenExpirationTime);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(now)
                .expiresAt(exp)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static String getCurrentUserEmail() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(context.getAuthentication())).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
    }

    public Jwt checkValidity(String token) {
        try {
            return this.jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }
}
