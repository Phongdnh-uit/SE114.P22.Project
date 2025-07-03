package com.se114p12.backend.dtos.nofitication;

import com.se114p12.backend.dtos.BaseResponseDTO;
import com.se114p12.backend.enums.NotificationType;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationResponseDTO extends BaseResponseDTO {
    private NotificationType type;
    private String title;
    private String message;
    private Integer status;
    private Long userId; // người nhận
    private Boolean isRead;
}