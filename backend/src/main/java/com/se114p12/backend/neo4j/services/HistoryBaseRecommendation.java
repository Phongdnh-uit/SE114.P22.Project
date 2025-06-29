package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.neo4j.enums.RecommendationSource;
import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HistoryBaseRecommendation implements RecommendationStrategy {

  private final ProductNeo4jRepository productRepository;

  @Override
  @Transactional
  public List<Long> getRecommendedProductIds(Long userId) {
    return productRepository.findRecommendedByHistory(userId);
  }

  @Override
  public Double getScore() {
    return RecommendationSource.HISTORY_BASED.getScore();
  }
}
