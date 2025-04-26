package com.se114p12.backend.domains.general;

import com.se114p12.backend.domains.BaseEntity;
import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "notifications")
@Accessors(chain = true)
public class Notification extends BaseEntity {
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
}
