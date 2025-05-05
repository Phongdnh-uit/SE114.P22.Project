package com.se114p12.backend.repository.order;

import com.se114p12.backend.domains.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
