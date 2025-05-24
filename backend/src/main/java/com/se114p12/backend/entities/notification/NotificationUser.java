package com.se114p12.backend.entities.notification;

import com.se114p12.backend.entities.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "notification_users")
@Data
public class NotificationUser {
    @EmbeddedId
    private NotificationUserId id = new NotificationUserId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("notificationId")
    private Notification notification;

    @NotNull
    private boolean isRead = false;
}
