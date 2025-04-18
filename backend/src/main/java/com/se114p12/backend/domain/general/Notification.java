package com.se114p12.backend.domain.general;

import com.se114p12.backend.domain.authentication.User;
import com.se114p12.backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@Entity
@Table(name = "notifications")
@Accessors(chain = true)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    //Liên kết (link) hoặc thông tin định hướng — có thể dùng để dẫn người dùng đến trang chi tiết khi nhấn vào thông báo.
//    @Column(name = "anchor")
//    private String anchor;

    // 2 trạng thái: 1 - Chưa đọc, 2 - Đã đọc
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
}
