package com.se114p12.backend.dtos.review;

import com.se114p12.backend.dtos.BaseResponseDTO;
import com.se114p12.backend.dtos.order.OrderResponseDTO;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ReviewResponseDTO extends BaseResponseDTO {
    private Long orderId;           // Chỉ cần ID của đơn hàng

    private Integer rate;          // Số sao đánh giá
    private String content;        // Nội dung đánh giá

    @Nullable
    private String reply;          // Phản hồi từ cửa hàng (nếu có)
}