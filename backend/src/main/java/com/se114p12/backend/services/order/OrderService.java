package com.se114p12.backend.services.order;

import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.dtos.order.OrderRequestDTO;
import com.se114p12.backend.dtos.order.OrderResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {
    PageVO<OrderResponseDTO> getAll(Specification<Order> specification, Pageable pageable);
    OrderResponseDTO getById(Long id);
    OrderResponseDTO create(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO);
    void OrderCancel(Long id);
    void delete(Long id);
}
