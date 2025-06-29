package com.se114p12.backend.neo4j.enums;

import lombok.Getter;

@Getter
public enum RecommendationSource {
  REORDERED(5.0),
  POPULAR(0.5),
  HISTORY_BASED(2.0),
  CATEGORY_BASED(1.0),
  CO_PURCHASE(1.5);

  private Double score;

  RecommendationSource(Double score) {
    this.score = score;
  }
}
