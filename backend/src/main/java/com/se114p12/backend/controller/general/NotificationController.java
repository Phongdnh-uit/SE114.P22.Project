package com.se114p12.backend.controller.general;

import com.se114p12.backend.dto.request.general.NotificationRequest;
import com.se114p12.backend.dto.request.general.NotificationResponse;
import com.se114p12.backend.service.general.NotificationService;
import com.se114p12.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> send(@RequestBody NotificationRequest request) {
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
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @PostMapping("/me/read-all")
    public ResponseEntity<Void> markAllMyNotificationsAsRead() {
        Long userId = jwtUtil.getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
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
