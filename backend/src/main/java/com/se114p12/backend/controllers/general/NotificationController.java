package com.se114p12.backend.controllers.general;

import com.se114p12.backend.dto.general.NotificationRequestDTO;
import com.se114p12.backend.dto.nofitication.NotificationResponse;
import com.se114p12.backend.services.general.NotificationService;
import com.se114p12.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> send(@RequestBody NotificationRequestDTO request) {
        return ResponseEntity.ok(notificationService.pushNotification(request));
    }

    // @GetMapping("/subscribe")
    // public SseEmitter subscribe() {
    //     String username = JwtUtil.getCurrentUserCredentials(); // Lấy username/email/phone từ token
    //     return notificationService.createEmitter(username);
    // }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        Long userId = jwtUtil.getCurrentUserId();
        List<NotificationResponse> responses = notificationService.getNotificationsByUserId(userId)
                .stream()
                .map(notification -> notificationService.toResponse(notification, userId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/me/read-all")
    public ResponseEntity<Void> markAllMyNotificationsAsRead() {
        Long userId = jwtUtil.getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Long userId = jwtUtil.getCurrentUserId();
        notificationService.markAsRead(userId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<Long> countMyUnreadNotifications() {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(notificationService.countUnreadByUserId(userId));
    }
}
