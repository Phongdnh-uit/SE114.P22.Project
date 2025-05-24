package com.se114p12.backend.entities.notification;

import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "notifications")
@Accessors(chain = true)
public class Notification extends BaseEntity {
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

    @Nullable
    private String targetUrl;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationUser> receivers = new ArrayList<>();
}
