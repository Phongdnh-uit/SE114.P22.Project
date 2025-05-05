package com.se114p12.backend.services.order;

import com.se114p12.backend.domains.order.Order;
import com.se114p12.backend.dto.order.OrderRequestDTO;
import com.se114p12.backend.dto.order.OrderResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {
    PageVO<OrderResponseDTO> getAll(Specification<Order> specification, Pageable pageable);
    OrderResponseDTO getById(Long id);
    OrderResponseDTO create(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO);
    void delete(Long id);
}
