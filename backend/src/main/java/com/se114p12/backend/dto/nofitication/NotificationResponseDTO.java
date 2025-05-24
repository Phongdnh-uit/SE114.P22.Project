package com.se114p12.backend.dto.nofitication;

import com.se114p12.backend.enums.NotificationType;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationResponseDTO {
    private Long id;
    private NotificationType type;
    private String title;
    private String message;
    private Integer status;
    private Instant createdAt;
    private Long userId; // người nhận
}