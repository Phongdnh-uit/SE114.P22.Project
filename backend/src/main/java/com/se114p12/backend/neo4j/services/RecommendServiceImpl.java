package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RecommendServiceImpl implements RecommendService {

  private final List<RecommendationStrategy> recommendationStrategies = new ArrayList<>();
  private final JwtUtil jwtUtil;

  // Injecting the recommendation strategies
  private final ReOrderRecommendation reOrderRecommendation;
  private final HistoryBaseRecommendation historyBaseRecommendation;
  private final CategoryBaseRecommendation categoryBaseRecommendation;
  private final CoPurchaseRecommendation coPurchaseRecommendation;
  private final PopularRecommendation popularRecommendation;

  // register recommendation strategies
  @PostConstruct
  private void init() {
    recommendationStrategies.add(reOrderRecommendation);
    recommendationStrategies.add(historyBaseRecommendation);
    recommendationStrategies.add(categoryBaseRecommendation);
    recommendationStrategies.add(coPurchaseRecommendation);
    recommendationStrategies.add(popularRecommendation);
  }

  @Transactional
  public List<Long> getRecommendProductIds() {
    Long userId = jwtUtil.getCurrentUserId();
    if (userId == null) throw new IllegalStateException("User is not authenticated");
    Map<Long, Double> productScores = new HashMap<>();
    for (RecommendationStrategy strategy : recommendationStrategies) {
      List<Long> recommendedProductIds = strategy.getRecommendedProductIds(userId);
      for (Long productId : recommendedProductIds) {
        productScores.put(
            productId, productScores.getOrDefault(productId, 0.0) + strategy.getScore());
      }
    }

    // Sort products by score in descending order
    return productScores.entrySet().stream()
        .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
        .map(Map.Entry::getKey)
        .toList();
  }
}
