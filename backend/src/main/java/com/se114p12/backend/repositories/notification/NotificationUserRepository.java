package com.se114p12.backend.repositories.notification;

import com.se114p12.backend.entities.notification.NotificationUser;
import com.se114p12.backend.entities.notification.NotificationUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, NotificationUserId> {
    List<NotificationUser> findByUserId(Long userId);
    long countByUserIdAndIsReadFalse(Long userId);
}