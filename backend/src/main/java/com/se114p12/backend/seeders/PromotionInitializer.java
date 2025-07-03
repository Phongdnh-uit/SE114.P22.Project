package com.se114p12.backend.seeders;

import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.repositories.promotion.PromotionRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PromotionInitializer {
  private final PromotionRepository promotionRepository;

  @Transactional
  public void initializePromotions() {
    List<Promotion> promotions = new ArrayList<>();

    // Măm chào bạn mới
    promotions.add(
        createPromotion(
            "WELCOME20",
            "Giảm 100k cho đơn hàng đầu tiên",
            BigDecimal.valueOf(200000),
            BigDecimal.ZERO));
    // Giảm giá cho đơn hàng trên 1 triệu
    promotions.add(
        createPromotion(
            "DISCOUNT10",
            "Giảm 100k cho đơn hàng trên 1 triệu",
            BigDecimal.valueOf(100000),
            BigDecimal.valueOf(1000000)));

    promotions.add(
        createPromotion(
            "FREESHIP",
            "Miễn phí vận chuyển cho đơn hàng trên 100k",
            BigDecimal.ZERO,
            BigDecimal.valueOf(500000)));

    promotionRepository.saveAll(promotions);
  }

  private Promotion createPromotion(
      String code, String description, BigDecimal discountValue, BigDecimal minValue) {
    Promotion promotion = new Promotion();
    promotion.setCode(code);
    promotion.setDescription(description);
    promotion.setDiscountValue(discountValue);
    promotion.setMinValue(minValue);
    promotion.setStartDate(Instant.now());
    promotion.setEndDate(Instant.now().plusSeconds(60 * 60 * 24 * 30)); // 30 days from now
    promotion.setIsPublic(true);
    return promotion;
  }
}
