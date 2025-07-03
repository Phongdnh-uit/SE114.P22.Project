package com.se114p12.backend.dtos.stats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueStatsResponseDTO {
  private String period;
  private Double totalRevenue;
  private Integer orderCount;
}
