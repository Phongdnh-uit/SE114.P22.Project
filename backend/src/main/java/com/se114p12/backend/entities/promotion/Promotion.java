package com.se114p12.backend.entities.promotion;

import com.se114p12.backend.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "promotions")
public class Promotion extends BaseEntity {

    private String description;

    @NotNull
    private BigDecimal discountValue;

    @NotNull
    private BigDecimal minValue;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    private String code;

    // true: toàn bộ người dùng, false: chỉ định người dùng
    private Boolean isPublic;
}