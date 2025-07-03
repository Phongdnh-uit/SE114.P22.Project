package com.se114p12.backend.repositories.notification;

import com.se114p12.backend.entities.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>,
        JpaSpecificationExecutor<Notification> {
    Page<Notification> findByIdIn(List<Long> ids, Specification<Notification> specification, Pageable pageable);
}