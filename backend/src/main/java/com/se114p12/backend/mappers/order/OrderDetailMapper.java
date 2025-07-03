package com.se114p12.backend.mappers.order;

import com.se114p12.backend.entities.order.OrderDetail;
import com.se114p12.backend.dtos.order.OrderDetailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(target = "totalPrice", expression = "java(orderDetail.getPrice().multiply(java.math.BigDecimal.valueOf(orderDetail.getQuantity())))")
    OrderDetailResponseDTO entityToResponseDTO(OrderDetail orderDetail);
}