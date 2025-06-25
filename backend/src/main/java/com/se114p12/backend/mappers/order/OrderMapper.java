package com.se114p12.backend.mappers.order;

import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.dtos.order.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "shipper.id", target = "shipperId")
    OrderResponseDTO entityToResponseDTO(Order order);

    // Ánh xạ từ Order entity sang orderId
    default Long map(Order order) {
        return order != null ? order.getId() : null;
    }
}
