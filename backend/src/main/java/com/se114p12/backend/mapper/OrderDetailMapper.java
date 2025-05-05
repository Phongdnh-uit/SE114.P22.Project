package com.se114p12.backend.mapper;

import com.se114p12.backend.domains.order.OrderDetail;
import com.se114p12.backend.dto.order.OrderDetailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "order.id", target = "orderId")
    OrderDetailResponseDTO entityToResponseDTO(OrderDetail orderDetail);
}
