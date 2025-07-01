package com.se114p12.backend.services.stats;

import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.enums.PaymentStatus;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

  private final ProductCategoryRepository productCategoryRepository;
  private final OrderRepository orderRepository;

  public List<RevenueStatsResponseDTO> getRevenueStats(Instant start, Instant end, String groupBy) {

    List<Order> orders = orderRepository.findByCreatedAtOptional(start, end);

    Map<String, List<Order>> groupedOrders = groupOrders(orders, groupBy);

    List<RevenueStatsResponseDTO> result = new ArrayList<>();

    for (Map.Entry<String, List<Order>> entry : groupedOrders.entrySet()) {
      String key = entry.getKey();
      List<Order> orderList = entry.getValue();
      RevenueStatsResponseDTO stats = new RevenueStatsResponseDTO();
      stats.setPeriod(key);
      stats.setTotalRevenue(
          orderList.stream().mapToDouble(order -> order.getTotalPrice().doubleValue()).sum());
      stats.setOrderCount(orderList.size());
      result.add(stats);
    }

    return result;
  }

  public Map<String, List<Order>> groupOrders(List<Order> orders, String groupBy) {
    Map<String, List<Order>> groupedOrders;

    switch (groupBy.toLowerCase()) {
      case "month":
        groupedOrders =
            orders.stream()
                .collect(
                    Collectors.groupingBy(
                        order -> YearMonth.from(order.getCreatedAt()).toString()));
        break;

      case "year":
        groupedOrders =
            orders.stream()
                .collect(
                    Collectors.groupingBy(order -> Year.from(order.getCreatedAt()).toString()));
        break;
      case "quarter":
        groupedOrders =
            orders.stream()
                .collect(
                    Collectors.groupingBy(
                        order -> {
                          LocalDate date = LocalDate.from(order.getCreatedAt());
                          int month = date.getMonthValue();
                          int quarter = (month - 1) / 3 + 1;
                          return "" + date.getYear() + "-Q" + quarter;
                        }));
        break;

      default:
        groupedOrders =
            orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().toString()));
    }

    return groupedOrders;
  }

  @Override
  public Map<Integer, BigDecimal> getRevenueStatsByYear(int year, String groupBy) {
    List<Order> orders = orderRepository.findByYear(year).stream()
            .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED &&
                    order.getPaymentStatus() == PaymentStatus.COMPLETED)
            .toList();

    switch (groupBy.toLowerCase()) {
      case "month":
        Map<Integer, BigDecimal> monthlyRevenue =
            orders.stream()
                .collect(
                    Collectors.groupingBy(
                        order ->
                            order.getCreatedAt().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.reducing(
                            BigDecimal.ZERO, Order::getTotalPrice, BigDecimal::add)));
        for (int month = 1; month <= 12; month++) {
          monthlyRevenue.putIfAbsent(month, BigDecimal.ZERO);
        }
        return monthlyRevenue;

      case "quarter":
        Map<Integer, BigDecimal> quarterlyRevenue =
            orders.stream()
                .collect(
                    Collectors.groupingBy(
                        order -> {
                          int month =
                              order.getCreatedAt().atZone(ZoneId.systemDefault()).getMonthValue();
                          return (month - 1) / 3 + 1; // Calculate quarter
                        },
                        Collectors.reducing(
                            BigDecimal.ZERO, Order::getTotalPrice, BigDecimal::add)));
        for (int quarter = 1; quarter <= 4; quarter++) {
          quarterlyRevenue.putIfAbsent(quarter, BigDecimal.ZERO);
        }
        return quarterlyRevenue;

      default:
        throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
    }
  }

  @Override
  public Map<String, BigDecimal> getSoldProductCountByCategory(int month, int year) {
    List<Order> orders = orderRepository.findByMonthAndYear(month, year).stream()
            .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED &&
                    order.getPaymentStatus() == PaymentStatus.COMPLETED)
            .toList();
    Map<Long, BigDecimal> soldByCategory =
        orders.stream()
            .flatMap(order -> order.getOrderDetails().stream())
            .collect(
                Collectors.groupingBy(
                    orderItem -> orderItem.getCategoryId(),
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        orderItem -> BigDecimal.valueOf(orderItem.getQuantity()),
                        BigDecimal::add)));
    List<ProductCategory> categories = productCategoryRepository.findAll();
    return categories.stream()
        .collect(
            Collectors.toMap(
                ProductCategory::getName,
                category -> soldByCategory.getOrDefault(category.getId(), BigDecimal.ZERO)));
  }

  @Override
  public Map<String, Long> getActiveOrderCountByStatus() {
    // Khởi tạo map với giá trị mặc định là 0
    Map<String, Long> result = new HashMap<>();
    result.put(OrderStatus.PENDING.name(), 0L);
    result.put(OrderStatus.CONFIRMED.name(), 0L);
    result.put(OrderStatus.PROCESSING.name(), 0L);

    List<Order> orders = orderRepository.findAll();

    // Đếm các status thực sự tồn tại
    Map<String, Long> counted = orders.stream()
            .filter(order -> {
              OrderStatus status = order.getOrderStatus();
              return status == OrderStatus.PENDING ||
                      status == OrderStatus.CONFIRMED ||
                      status == OrderStatus.PROCESSING;
            })
            .collect(Collectors.groupingBy(
                    order -> order.getOrderStatus().name(),
                    Collectors.counting()
            ));

    // Gộp giá trị đếm vào map mặc định
    result.putAll(counted);
    return result;
  }
}
