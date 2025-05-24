package com.se114p12.backend.controllers.promotion;

import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.services.promotion.UserPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-promotions")
@RequiredArgsConstructor
public class UserPromotionController {

    private final UserPromotionService userPromotionService;

    @GetMapping("/available")
    public List<Promotion> getAvailablePromotions(@RequestParam Long userId) {
        return userPromotionService.getAvailablePromotions(userId);
    }

    @GetMapping("/available-for-order")
    public List<Promotion> getAvailablePromotionsForOrder(@RequestParam Long userId, @RequestParam Double orderValue) {
        return userPromotionService.getAvailablePromotionsForOrder(userId, orderValue);
    }

    @PostMapping("/use")
    public void markPromotionAsUsed(@RequestParam Long userId, @RequestParam Long promotionId) {
        userPromotionService.markPromotionAsUsed(userId, promotionId);
    }
}
