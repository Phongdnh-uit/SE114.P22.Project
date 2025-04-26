package com.se114p12.backend.repository.general;

import com.se114p12.backend.domains.general.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>,
        JpaSpecificationExecutor<Notification> {

    List<Notification> findByUser_Id(Long userId);

    List<Notification> findByUser_Username(String username);

    long countByUser_IdAndStatus(Long userId, Integer status);
}