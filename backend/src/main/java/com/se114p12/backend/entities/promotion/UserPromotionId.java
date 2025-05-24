package com.se114p12.backend.entities.promotion;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPromotionId implements Serializable {
    private Long userId;
    private Long promotionId;
}
