package com.se114p12.backend.entities.promotion;

import com.se114p12.backend.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_promotion")
public class UserPromotion {
    @EmbeddedId
    private UserPromotionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("promotionId")
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "is_used")
    private boolean isUsed = false;
}