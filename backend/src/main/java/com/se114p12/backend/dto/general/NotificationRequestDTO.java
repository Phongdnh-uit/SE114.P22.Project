package com.se114p12.backend.dto.general;

import com.se114p12.backend.enums.NotificationType;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class NotificationRequestDTO {
    private List<Long> userIds;
    private NotificationType type;
    private String title;
    private String message;

    @Nullable
    private Integer status;
}