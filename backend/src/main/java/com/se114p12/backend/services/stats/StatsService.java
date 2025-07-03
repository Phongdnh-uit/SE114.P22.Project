package com.se114p12.backend.services.stats;

import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface StatsService {

  List<RevenueStatsResponseDTO> getRevenueStats(Instant startDate, Instant endDate, String groupBy);

  Map<Integer, BigDecimal> getRevenueStatsByYear(int year, String groupBy);

  Map<String, BigDecimal> getSoldProductCountByCategory(int month, int year);

  Map<String, Long> getActiveOrderCountByStatus();
}
