package com.se114p12.backend.services.general;

import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.domains.general.Notification;
import com.se114p12.backend.dto.request.general.NotificationRequest;
import com.se114p12.backend.dto.response.response.NotificationResponse;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.repository.general.EmitterRepository;
import com.se114p12.backend.repository.general.NotificationRepository;
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

    public NotificationResponse pushNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = toEntity(request, user);
        Notification saved = notificationRepository.save(notification);

        NotificationResponse response = toResponse(saved);

        emitterRepository.get(user.getUsername()).ifPresent(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(response));
            } catch (IOException e) {
                log.warn("Failed to send notification to user {}: {}", user.getUsername(), e.getMessage());
                emitterRepository.remove(user.getUsername());
            }
        });

        return response;
    }

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUser_Id(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(2); // 2 = Đã đọc
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_Id(userId);
        notifications.forEach(notification -> notification.setStatus(2)); // 2 = Đã đọc
        notificationRepository.saveAll(notifications);
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found");
        }
        notificationRepository.deleteById(id);
    }

    public long countUnreadByUserId(Long userId) {
        return notificationRepository.findByUser_Id(userId).stream()
                .filter(notification -> notification.getStatus() == 1)
                .count();
    }

    public SseEmitter createEmitter(String username) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1 hour
        emitterRepository.addOrReplace(username, emitter);
        emitter.onTimeout(() -> emitterRepository.remove(username));
        emitter.onCompletion(() -> emitterRepository.remove(username));
        return emitter;
    }

    private Notification toEntity(NotificationRequest request, User user) {
        return new Notification()
                .setUser(user)
                .setType(request.getType())
                .setTitle(request.getTitle())
                .setMessage(request.getMessage())
                .setStatus(request.getStatus());
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}