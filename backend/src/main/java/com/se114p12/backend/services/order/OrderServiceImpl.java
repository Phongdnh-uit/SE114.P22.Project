package com.se114p12.backend.services.order;

import com.se114p12.backend.domains.cart.Cart;
import com.se114p12.backend.domains.cart.CartItem;
import com.se114p12.backend.domains.order.Order;
import com.se114p12.backend.domains.order.OrderDetail;
import com.se114p12.backend.domains.product.Product;
import com.se114p12.backend.dto.order.OrderRequestDTO;
import com.se114p12.backend.dto.order.OrderResponseDTO;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.enums.PaymentStatus;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.mappers.OrderDetailMapper;
import com.se114p12.backend.mappers.OrderMapper;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.repository.cart.CartItemRepository;
import com.se114p12.backend.repository.cart.CartRepository;
import com.se114p12.backend.repository.order.OrderDetailRepository;
import com.se114p12.backend.repository.order.OrderRepository;
import com.se114p12.backend.repository.product.ProductRepository;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public PageVO<OrderResponseDTO> getAll(Specification<Order> specification, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        return PageVO.<OrderResponseDTO>builder()
                .content(orders.getContent().stream().map(orderMapper::entityToResponseDTO).toList())
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .numberOfElements(orders.getNumberOfElements())
                .page(orders.getNumber())
                .size(orders.getSize())
                .build();
    }

    @Override
    public OrderResponseDTO getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.entityToResponseDTO(order);
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        Long currentUserId = jwtUtil.getCurrentUserId();
        Cart cart = cartRepository.findByUserId(currentUserId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Order order = new Order();
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setNote(orderRequestDTO.getNote());
        order.setUser(userRepository.findById(currentUserId).orElseThrow(() -> new ResourceNotFoundException("User not found")));
        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderDetail> orderDetails;
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductImage(product.getImageUrl());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setVariationInfo(extractVariationInfo(cartItem));
            orderDetail.setPrice(cartItem.getPrice());
            totalPrice = totalPrice.add(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            order.getOrderDetails().add(orderDetail);

            // delete cart item
            cartItemRepository.delete(cartItem);
        }

        order.setTotalPrice(totalPrice.longValue());
        return orderMapper.entityToResponseDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private String extractVariationInfo(CartItem cartItem) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        cartItem.getVariationOptions()
                .forEach(variationOption -> {
                    var info = Map.<String, String>of(variationOption.getVariation().getName(), variationOption.getValue());
                    stringBuilder.append(info.toString()).append(", ");
                });
        return stringBuilder.append("]").toString();
    }
}
