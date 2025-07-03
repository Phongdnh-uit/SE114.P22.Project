package com.se114p12.backend.services.promotion;

import com.se114p12.backend.entities.promotion.Promotion;

import java.math.BigDecimal;
import java.util.List;

public interface UserPromotionService {
    List<Promotion> getAvailablePromotions(Long userId);
    Promotion applyPromotion(Long userId, Long promotionId, BigDecimal orderValue);
    List<Promotion> getAvailablePromotionsForOrder(Long userId, BigDecimal orderValue);
    void markPromotionAsUsed(Long userId, Long promotionId);
}
