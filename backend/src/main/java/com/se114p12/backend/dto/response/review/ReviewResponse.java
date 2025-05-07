package com.se114p12.backend.dto.response.review;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
public class ReviewResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;

    private UserResponse user;     // Người viết đánh giá
    private OrderResponse order;   // Đơn hàng được đánh giá

    private Integer rate;          // Số sao đánh giá
    private String content;        // Nội dung đánh giá

    @Nullable
    private String reply;          // Phản hồi từ cửa hàng (nếu có)

    @Data
    public static class UserResponse {
        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String username;
        private String fullname;
        private String avatarUrl;
    }

    @Data
    public static class OrderResponse {
        private Long id;
        private Instant orderDate;
        private String shippingAddress;
        private Long totalPrice;
        private String note;
        private Instant expectedDeliveryTime;
        private Instant actualDeliveryTime;
        private String orderStatus;
    }
}