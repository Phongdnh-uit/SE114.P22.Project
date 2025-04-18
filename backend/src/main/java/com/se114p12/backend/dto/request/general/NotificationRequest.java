package com.se114p12.backend.dto.request.general;

import com.se114p12.backend.enums.NotificationType;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class NotificationRequest {
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    @Nullable
//    private String anchor;
    private Integer status; // 1 - Chưa đọc, 2 - Đã đọc (hoặc mặc định là 1 nếu cần)
}