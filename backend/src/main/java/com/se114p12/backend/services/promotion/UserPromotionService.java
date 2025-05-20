package com.se114p12.backend.services.promotion;

import com.se114p12.backend.entities.promotion.Promotion;

import java.util.List;

public interface UserPromotionService {
    List<Promotion> getAvailablePromotions(Long userId);
    List<Promotion> getAvailablePromotionsForOrder(Long userId, Double orderValue);
    void markPromotionAsUsed(Long userId, Long promotionId);
}
