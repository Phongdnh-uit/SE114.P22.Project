package com.se114p12.backend.controllers.order;

import com.se114p12.backend.domains.order.Order;
import com.se114p12.backend.dto.order.OrderRequestDTO;
import com.se114p12.backend.dto.order.OrderResponseDTO;
import com.se114p12.backend.services.order.OrderService;
import com.se114p12.backend.vo.PageVO;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageVO<OrderResponseDTO>> getAllOrders(Pageable pageable, @Filter Specification<Order> specification) {
        return ResponseEntity.ok(orderService.getAll(specification, pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.ok(orderService.create(orderRequestDTO));
    }

    @PutMapping("/update")
    public String updateOrder(Long id) {
        return orderService.update(id, null).toString();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
