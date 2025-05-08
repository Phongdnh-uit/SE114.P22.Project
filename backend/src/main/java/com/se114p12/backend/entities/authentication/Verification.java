package com.se114p12.backend.entities.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.enums.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "verifications")
public class Verification extends BaseEntity {
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private Instant expiredAt;

    @Enumerated(EnumType.STRING)
    private VerificationType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
