package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.neo4j.enums.RecommendationSource;
import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReOrderRecommendation implements RecommendationStrategy {

  private final ProductNeo4jRepository productRepository;

  @Override
  public Double getScore() {
    return RecommendationSource.REORDERED.getScore();
  }

  @Override
  public List<Long> getRecommendedProductIds(Long userId) {
    return productRepository.findReOrderRecommendation(userId);
  }
}
