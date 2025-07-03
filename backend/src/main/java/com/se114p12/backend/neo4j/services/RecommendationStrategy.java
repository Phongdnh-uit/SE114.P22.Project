package com.se114p12.backend.neo4j.services;

import java.util.List;

public interface RecommendationStrategy {
  List<Long> getRecommendedProductIds(Long userId);

  Double getScore();
}
