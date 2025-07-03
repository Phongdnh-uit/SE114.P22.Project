package com.se114p12.backend.controllers.stats;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import com.se114p12.backend.services.stats.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Statistics Module",
    description = "Endpoints for statistical data (user, order, product, revenue)")
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.API_BASE_PATH + "/stats")
public class StatsController {

  private final StatsService statsService;

  @Operation(
      summary = "Get revenue statistics",
      description =
          "Returns revenue statistics grouped by day, week, or month within a specific time range.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of revenue statistics",
            content =
                @Content(
                    array =
                        @ArraySchema(
                            schema = @Schema(implementation = RevenueStatsResponseDTO.class))))
      })
  @ErrorResponse
  @GetMapping("/revenue")
  public ResponseEntity<List<RevenueStatsResponseDTO>> getRevenueStats(
      @Parameter(
              description = "Start date in ISO-8601 format (e.g., 2024-01-01T00:00:00Z)",
              required = false)
          @RequestParam(value = "startDate", required = false)
          Instant startDate,
      @Parameter(
              description = "End date in ISO-8601 format (e.g., 2024-01-31T23:59:59Z)",
              required = false)
          @RequestParam(value = "endDate", required = false)
          Instant endDate,
      @Parameter(
              description = "Group by 'day', 'week', or 'month'",
              example = "month",
              required = false)
          @RequestParam(value = "groupBy", required = false)
          String groupBy) {
    return ResponseEntity.ok(statsService.getRevenueStats(startDate, endDate, groupBy));
  }

  @Operation(
      summary = "Get revenue statistics by month or quarter of the year",
      description = "Returns revenue statistics grouped by day by month or quarter of year.")
  @GetMapping("/revenue/month-or-quarter")
  public ResponseEntity<Map<Integer, BigDecimal>> getRevenueStatsForMonthOrQuarter(
      @Parameter(description = "Year for which to retrieve statistics", required = true)
          @RequestParam(value = "year")
          int year,
      @Parameter(description = "groupBy can be 'month' or 'quarter'", required = true)
          @RequestParam(value = "groupBy")
          String groupBy) {
    return ResponseEntity.ok(statsService.getRevenueStatsByYear(year, groupBy));
  }

  @Operation(
      summary = "Get revenue statistics by year",
      description = "Returns revenue statistics grouped by year.")
  @GetMapping("/get-sold-by-category")
  public ResponseEntity<Map<String, BigDecimal>> getSoldByCategory(
      @Parameter(description = "Month for which to retrieve statistics", required = true)
          @RequestParam(value = "month")
          int month,
      @Parameter(description = "Year for which to retrieve statistics", required = true)
          @RequestParam(value = "year")
          int year) {
    return ResponseEntity.ok(statsService.getSoldProductCountByCategory(month, year));
  }

    @Operation(
            summary = "Get total order count for statuses: PENDING, CONFIRMED, PROCESSING",
            description = "Returns the number of orders in PENDING, CONFIRMED, and PROCESSING states.")
    @GetMapping("/total-order-count-by-status/active")
    public ResponseEntity<Map<String, Long>> getActiveOrderCountByStatus() {
        return ResponseEntity.ok(statsService.getActiveOrderCountByStatus());
    }
}
