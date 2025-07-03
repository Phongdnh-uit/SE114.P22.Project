package com.se114p12.backend.repositories.order;

import com.se114p12.backend.entities.order.Order;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
    extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

  @Query(
      "SELECT o FROM Order o WHERE (:start IS NULL OR o.createdAt >= :start) AND (:end IS NULL OR"
          + " o.createdAt <= :end)")
  List<Order> findByCreatedAtOptional(@Param("start") Instant start, @Param("end") Instant end);

  @Query("SELECT o FROM Order o WHERE YEAR(o.createdAt) = :year")
  List<Order> findByYear(int year);

  @Query("SELECT o FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
  List<Order> findByMonthAndYear(int month, int year);

    Optional<Order> findByTxnRef(String txnRef);
}
