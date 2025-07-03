package com.se114p12.backend.mappers.review;

import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.entities.review.Review;
import com.se114p12.backend.mappers.order.OrderMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface ReviewMapper {
    @Mapping(source = "order", target = "orderId")
    ReviewResponseDTO toResponse(Review review);
}