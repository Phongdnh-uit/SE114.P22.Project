package com.se114p12.backend.services.order;

import com.se114p12.backend.dtos.order.OrderRequestDTO;
import com.se114p12.backend.dtos.order.OrderResponseDTO;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {
  PageVO<OrderResponseDTO> getAll(Specification<Order> specification, Pageable pageable);

  OrderResponseDTO getById(Long id);

  PageVO<OrderResponseDTO> getOrdersByUserId(
      Long userId, Specification<Order> specification, Pageable pageable);

  OrderResponseDTO create(OrderRequestDTO orderRequestDTO);

  OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO);

  void updateStatus(Long id, OrderStatus status);

  void cancelOrder(Long id);

  void delete(Long id);

  void markOrderAsDelivered(Long orderId);

<<<<<<< HEAD

  Long markPaymentFailed(String txnRef);
=======
  void markPaymentFailed(String txnRef);
>>>>>>> 385cd0302588c99f66047e44e981f71f0181c554

  Long markPaymentCompleted(String txnRef);
}
