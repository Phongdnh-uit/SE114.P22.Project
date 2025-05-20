package com.se114p12.backend.services.notification;

import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.entities.notification.Notification;
import com.se114p12.backend.entities.notification.NotificationUser;
import com.se114p12.backend.entities.notification.NotificationUserId;
import com.se114p12.backend.dto.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dto.nofitication.NotificationResponseDTO;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.repository.notification.EmitterRepository;
import com.se114p12.backend.repository.notification.NotificationRepository;
import com.se114p12.backend.repository.notification.NotificationUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;
    private final NotificationUserRepository notificationUserRepository;

    public NotificationResponseDTO pushNotification(NotificationRequestDTO request) {
        Notification notification = new Notification()
                .setType(request.getType())
                .setTitle(request.getTitle())
                .setMessage(request.getMessage())
                .setStatus(request.getStatus() != null ? request.getStatus() : 1);

        notification = notificationRepository.save(notification);

        for (Long userId : request.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setNotification(notification);
            notificationUser.setUser(user);
            notification.getReceivers().add(notificationUser);

            Notification finalNotification = notification;

            emitterRepository.get(user.getUsername()).ifPresent(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(toResponse(finalNotification, user.getId())));
                } catch (IOException e) {
                    log.warn("Failed to send notification to {}", user.getUsername());
                    emitterRepository.remove(user.getUsername());
                }
            });
        }

        notificationRepository.save(notification); // lưu lại với receivers

        return toResponse(notification, null); // trả thông tin chung
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationUserRepository.findByUserId(userId)
                .stream()
                .map(NotificationUser::getNotification)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long userId, Long notificationId) {
        NotificationUser nu = notificationUserRepository.findById(new NotificationUserId(userId, notificationId))
                .orElseThrow(() -> new RuntimeException("Notification not found for user"));
        nu.setRead(true);
        notificationUserRepository.save(nu);
    }

    public void markAllAsRead(Long userId) {
        List<NotificationUser> notificationUsers = notificationUserRepository.findByUserId(userId);
        for (NotificationUser nu : notificationUsers) {
            if (!nu.isRead()) {
                nu.setRead(true);
            }
        }
        notificationUserRepository.saveAll(notificationUsers);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public long countUnreadByUserId(Long userId) {
        return notificationUserRepository.countByUserIdAndIsReadFalse(userId);
    }

    public NotificationResponseDTO toResponse(Notification notification, Long userId) {
        NotificationResponseDTO response = new NotificationResponseDTO();
        response.setId(notification.getId());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUserId(userId);
        return response;
    }
}