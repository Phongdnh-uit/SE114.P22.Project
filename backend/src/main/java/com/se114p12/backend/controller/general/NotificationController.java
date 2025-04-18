package com.se114p12.backend.controller.general;

import com.se114p12.backend.dto.request.general.NotificationRequest;
import com.se114p12.backend.dto.request.general.NotificationResponse;
import com.se114p12.backend.service.general.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> send(@RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.pushNotification(request));
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam String username) {
        return notificationService.createEmitter(username);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @PostMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
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

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> countUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.countUnreadByUserId(userId));
    }
}