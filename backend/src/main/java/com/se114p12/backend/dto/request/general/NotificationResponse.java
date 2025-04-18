package com.se114p12.backend.dto.request.general;

import com.se114p12.backend.enums.NotificationType;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String title;
    private String message;
    @Nullable
//    private String anchor;
    private Integer status;
    private Instant createdAt;
}