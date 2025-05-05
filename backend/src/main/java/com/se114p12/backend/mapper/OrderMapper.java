package com.se114p12.backend.mapper;

import com.se114p12.backend.domains.order.Order;
import com.se114p12.backend.dto.order.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDTO entityToResponseDTO(Order order);
}
