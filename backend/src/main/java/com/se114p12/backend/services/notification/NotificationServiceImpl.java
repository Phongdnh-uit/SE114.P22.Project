package com.se114p12.backend.services.notification;

import com.se114p12.backend.dtos.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dtos.nofitication.NotificationResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.notification.Notification;
import com.se114p12.backend.entities.notification.NotificationUser;
import com.se114p12.backend.entities.notification.NotificationUserId;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.mappers.notification.NotificationMapper;
import com.se114p12.backend.repositories.authentication.RoleRepository;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.notification.EmitterRepository;
import com.se114p12.backend.repositories.notification.NotificationRepository;
import com.se114p12.backend.repositories.notification.NotificationUserRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmitterRepository emitterRepository;
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponseDTO pushNotification(NotificationRequestDTO request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("UserIds cannot be null or empty for pushNotification");
        }

        Notification notification = new Notification()
                .setType(request.getType())
                .setTitle(request.getTitle())
                .setMessage(request.getMessage());

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

        notificationRepository.save(notification); // Save with receivers

        return toResponse(notification, null);
    }

    @Override
    public NotificationResponseDTO sendToAll(NotificationRequestDTO request) {
        Notification notification = new Notification()
                .setType(request.getType())
                .setTitle(request.getTitle())
                .setMessage(request.getMessage());

        notification = notificationRepository.save(notification);

        // Tìm tất cả Role có name là USER
        Role userRole = roleRepository.findByName(RoleName.USER.toString())
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        List<User> users = userRepository.findByRole(userRole);

        for (User user : users) {
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

        notificationRepository.save(notification);

        return toResponse(notification, null);
    }

    @Override
    public PageVO<NotificationResponseDTO> getAll(Specification<Notification> specification, Pageable pageable) {
        Page<Notification> page = notificationRepository.findAll(specification, pageable);

        List<NotificationResponseDTO> content = page
                .map(notification -> toResponse(notification, null))
                .getContent();

        return PageVO.<NotificationResponseDTO>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .content(content)
                .build();
    }

    @Override
    public PageVO<NotificationResponseDTO> getNotificationsByUserId(Long userId, Specification<Notification> specification, Pageable pageable) {
        // Lấy danh sách NotificationUser theo userId
        List<NotificationUser> userNotifications = notificationUserRepository.findByUserId(userId);
        List<Long> notificationIds = userNotifications.stream()
                .map(nu -> nu.getNotification().getId())
                .toList();

        // Tạo Specification để lọc theo notificationIds và điều kiện từ FE
        Specification<Notification> finalSpec = (root, query, cb) -> root.get("id").in(notificationIds);
        if (specification != null) {
            finalSpec = finalSpec.and(specification);
        }

        // Truy vấn danh sách thông báo
        Page<Notification> page = notificationRepository.findAll(finalSpec, pageable);

        // Map notificationId -> NotificationUser (để lấy nhanh trạng thái isRead)
        Map<Long, Boolean> readStatusMap = userNotifications.stream()
                .collect(Collectors.toMap(
                        nu -> nu.getNotification().getId(),
                        NotificationUser::isRead
                ));

        // Chuyển từng Notification thành DTO có isRead
        List<NotificationResponseDTO> content = page.getContent().stream()
                .map(notification -> {
                    NotificationResponseDTO dto = notificationMapper.toDTO(notification);
                    dto.setId(notification.getId());
                    dto.setCreatedAt(notification.getCreatedAt());
                    dto.setUpdatedAt(notification.getUpdatedAt());
                    dto.setUserId(userId);
                    dto.setIsRead(readStatusMap.getOrDefault(notification.getId(), false));
                    return dto;
                })
                .toList();

        return PageVO.<NotificationResponseDTO>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .content(content)
                .build();
    }

    @Override
    public void markAsRead(Long userId, Long notificationId) {
        NotificationUser nu = notificationUserRepository.findById(new NotificationUserId(userId, notificationId))
                .orElseThrow(() -> new RuntimeException("Notification not found for user"));
        nu.setRead(true);
        notificationUserRepository.save(nu);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<NotificationUser> notificationUsers = notificationUserRepository.findByUserId(userId);
        for (NotificationUser nu : notificationUsers) {
            if (!nu.isRead()) {
                nu.setRead(true);
            }
        }
        notificationUserRepository.saveAll(notificationUsers);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public long countUnreadByUserId(Long userId) {
        return notificationUserRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public NotificationResponseDTO toResponse(Notification notification, Long userId) {
        NotificationResponseDTO dto = notificationMapper.toDTO(notification);
        dto.setId(notification.getId());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        dto.setUserId(userId);

        if (userId != null) {
            NotificationUserId id = new NotificationUserId(userId, notification.getId());
            notificationUserRepository.findById(id).ifPresent(nu -> dto.setIsRead(nu.isRead()));
        }

        return dto;
    }
}