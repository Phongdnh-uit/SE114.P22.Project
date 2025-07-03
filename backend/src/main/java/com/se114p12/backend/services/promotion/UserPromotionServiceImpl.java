package com.se114p12.backend.services.promotion;

import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.entities.promotion.UserPromotion;
import com.se114p12.backend.entities.promotion.UserPromotionId;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.promotion.PromotionRepository;
import com.se114p12.backend.repositories.promotion.UserPromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPromotionServiceImpl implements UserPromotionService {

    private final UserRepository userRepository;
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

        Set<Promotion> availablePrivatePromotions = userPromotions.stream()
                .filter(up -> !up.isUsed())
                .map(UserPromotion::getPromotion)
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .collect(Collectors.toSet());

        Set<Promotion> availablePublicPromotions = promotionRepository.findByIsPublicTrue().stream()
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .filter(p -> !usedPromotionIds.contains(p.getId()))
                .collect(Collectors.toSet());

        Set<Promotion> allAvailable = new HashSet<>(availablePrivatePromotions);
        allAvailable.addAll(availablePublicPromotions);

        return new ArrayList<>(allAvailable);
    }

    @Override
    @Transactional
    public Promotion applyPromotion(Long userId, Long promotionId, BigDecimal orderValue) {
        Instant now = Instant.now();

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        if (now.isBefore(promotion.getStartDate()) || now.isAfter(promotion.getEndDate())) {
            throw new IllegalArgumentException("Promotion is not active at this time");
        }

        if (orderValue.compareTo(promotion.getMinValue()) < 0) {
            throw new IllegalArgumentException("Order value does not meet the promotion's minimum requirement");
        }

        // Kiểm tra nếu đã dùng rồi thì không cho áp dụng mã giảm giá đang truyền vào nữa
        if (!promotion.getIsPublic()) {
            Optional<UserPromotion> up = userPromotionRepository.findByUserIdAndPromotionId(userId, promotionId);
            if (up.isEmpty()) {
                throw new IllegalArgumentException("This private promotion is not assigned to the user.");
            }
            if (up.get().isUsed()) {
                throw new IllegalArgumentException("This promotion has already been used.");
            }
        } else {
            boolean alreadyUsed = userPromotionRepository.findByUserIdAndPromotionId(userId, promotionId)
                    .map(UserPromotion::isUsed)
                    .orElse(false);
            if (alreadyUsed) {
                throw new IllegalArgumentException("This public promotion has already been used.");
            }
        }

        // Cập nhật là đã dùng mã giảm giá
        updatePromotionUsage(userId, promotionId);

        return promotion;
    }

    @Override
    @Transactional
    public void markPromotionAsUsed(Long userId, Long promotionId) {
        updatePromotionUsage(userId, promotionId);
    }

    @Override
    public List<Promotion> getAvailablePromotionsForOrder(Long userId, BigDecimal orderValue) {
        Instant now = Instant.now();

        Set<Long> usedPromotionIds = userPromotionRepository.findByUserId(userId).stream()
                .filter(UserPromotion::isUsed)
                .map(up -> up.getPromotion().getId())
                .collect(Collectors.toSet());

        Set<Promotion> privateEligible = userPromotionRepository.findByUserId(userId).stream()
                .filter(up -> !up.isUsed())
                .map(UserPromotion::getPromotion)
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .filter(p -> orderValue.compareTo(p.getMinValue()) >= 0)
                .collect(Collectors.toSet());

        Set<Promotion> publicEligible = promotionRepository.findByIsPublicTrue().stream()
                .filter(p -> !now.isBefore(p.getStartDate()) && !now.isAfter(p.getEndDate()))
                .filter(p -> !usedPromotionIds.contains(p.getId()))
                .filter(p -> orderValue.compareTo(p.getMinValue()) >= 0)
                .collect(Collectors.toSet());

        Set<Promotion> allEligible = new HashSet<>(privateEligible);
        allEligible.addAll(publicEligible);

        return new ArrayList<>(allEligible);
    }

    private void updatePromotionUsage(Long userId, Long promotionId) {
        userPromotionRepository.findByUserIdAndPromotionId(userId, promotionId)
                .ifPresentOrElse(
                        userPromotion -> {
                            if (!userPromotion.isUsed()) {
                                userPromotion.setUsed(true);
                                userPromotionRepository.save(userPromotion);
                            }
                        },
                        () -> {
                            Promotion promotion = promotionRepository.findById(promotionId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

                            if (!promotion.getIsPublic()) {
                                throw new IllegalArgumentException("Cannot auto-assign private promotion");
                            }

                            User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                            UserPromotion newUserPromotion = new UserPromotion();
                            newUserPromotion.setId(new UserPromotionId(userId, promotionId));
                            newUserPromotion.setUser(user);
                            newUserPromotion.setPromotion(promotion);
                            newUserPromotion.setUsed(true);

                            userPromotionRepository.save(newUserPromotion);

                        }
                );
    }
}