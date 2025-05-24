package com.se114p12.backend.services.promotion;

import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.entities.promotion.UserPromotion;
import com.se114p12.backend.entities.promotion.UserPromotionId;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.repository.promotion.PromotionRepository;
import com.se114p12.backend.repository.promotion.UserPromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPromotionServiceImpl implements UserPromotionService {

    private final UserPromotionRepository userPromotionRepository;
    private final PromotionRepository promotionRepository;

    @Override
    public List<Promotion> getAvailablePromotions(Long userId) {
        Instant now = Instant.now();

        List<UserPromotion> userPromotions = userPromotionRepository.findByUserId(userId);
        Set<Long> usedPromotionIds = userPromotions.stream()
                .filter(UserPromotion::isUsed)
                .map(up -> up.getPromotion().getId())
                .collect(Collectors.toSet());

        Set<Long> availablePromotionIds = userPromotions.stream()
                .filter(up -> !up.isUsed())
                .map(up -> up.getPromotion().getId())
                .collect(Collectors.toSet());

        return promotionRepository.findAll().stream()
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .filter(p -> !usedPromotionIds.contains(p.getId())) // Loại bỏ những cái đã dùng
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markPromotionAsUsed(Long userId, Long promotionId) {
        Optional<UserPromotion> existing = userPromotionRepository.findByUserIdAndPromotionId(userId, promotionId);

        if (existing.isPresent()) {
            UserPromotion userPromotion = existing.get();
            userPromotion.setUsed(true);
            userPromotionRepository.save(userPromotion);
        } else {
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));

            User user = new User();
            user.setId(userId);

            UserPromotion newUserPromotion = new UserPromotion();
            newUserPromotion.setId(new UserPromotionId(userId, promotionId));
            newUserPromotion.setUser(user);
            newUserPromotion.setPromotion(promotion);
            newUserPromotion.setUsed(true);

            userPromotionRepository.save(newUserPromotion);
        }
    }

    @Override
    public List<Promotion> getAvailablePromotionsForOrder(Long userId, Double orderValue) {
        Instant now = Instant.now();

        List<UserPromotion> userPromotions = userPromotionRepository.findByUserId(userId);
        Set<Long> usedPromotionIds = userPromotions.stream()
                .filter(UserPromotion::isUsed)
                .map(up -> up.getPromotion().getId())
                .collect(Collectors.toSet());

        return promotionRepository.findAll().stream()
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .filter(p -> orderValue >= p.getMinValue())
                .filter(p -> !usedPromotionIds.contains(p.getId()))
                .collect(Collectors.toList());
    }
}