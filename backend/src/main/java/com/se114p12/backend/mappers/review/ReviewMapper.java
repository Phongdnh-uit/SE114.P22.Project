package com.se114p12.backend.mapper;

import com.se114p12.backend.domains.review.Review;
import com.se114p12.backend.dto.response.review.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mappings({
            @Mapping(source = "user.id", target = "user.id"),
            @Mapping(source = "user.createdAt", target = "user.createdAt"),
            @Mapping(source = "user.updatedAt", target = "user.updatedAt"),
            @Mapping(source = "user.username", target = "user.username"),
            @Mapping(source = "user.fullname", target = "user.fullname"),
            @Mapping(source = "user.avatarUrl", target = "user.avatarUrl"),

            @Mapping(source = "order.id", target = "order.id"),
            @Mapping(source = "order.orderDate", target = "order.orderDate"),
            @Mapping(source = "order.shippingAddress", target = "order.shippingAddress"),
            @Mapping(source = "order.totalPrice", target = "order.totalPrice"),
            @Mapping(source = "order.note", target = "order.note"),
            @Mapping(source = "order.expectedDeliveryTime", target = "order.expectedDeliveryTime"),
            @Mapping(source = "order.actualDeliveryTime", target = "order.actualDeliveryTime"),
            @Mapping(source = "order.orderStatus", target = "order.orderStatus")
    })
    ReviewResponse toResponse(Review review);
}