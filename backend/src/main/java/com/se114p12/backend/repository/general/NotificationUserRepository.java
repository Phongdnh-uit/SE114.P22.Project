package com.se114p12.backend.repository.general;

import com.se114p12.backend.domains.general.NotificationUser;
import com.se114p12.backend.domains.general.NotificationUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, NotificationUserId> {
    List<NotificationUser> findByUserId(Long userId);
    long countByUserIdAndIsReadFalse(Long userId);
}