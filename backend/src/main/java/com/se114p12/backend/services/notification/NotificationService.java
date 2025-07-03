package com.se114p12.backend.services.notification;

import com.se114p12.backend.dtos.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dtos.nofitication.NotificationResponseDTO;
import com.se114p12.backend.entities.notification.Notification;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface NotificationService {
    NotificationResponseDTO pushNotification(NotificationRequestDTO request);
    NotificationResponseDTO sendToAll(NotificationRequestDTO request);
    PageVO<NotificationResponseDTO> getAll(Specification<Notification> specification, Pageable pageable);
    PageVO<NotificationResponseDTO> getNotificationsByUserId(Long userId, Specification<Notification> specification, Pageable pageable);
    void markAsRead(Long userId, Long notificationId);
    void markAllAsRead(Long userId);
    void deleteNotification(Long notificationId);
    long countUnreadByUserId(Long userId);
    NotificationResponseDTO toResponse(Notification notification, Long userId);
}
