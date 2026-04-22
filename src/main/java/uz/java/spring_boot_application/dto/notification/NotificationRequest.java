package uz.java.spring_boot_application.dto.notification;

import lombok.Data;
import uz.java.spring_boot_application.entities.NotificationEntity;

@Data
public class NotificationRequest {
    String title;
    String body;
    NotificationEntity.NotificationType type;

    public NotificationRequest(String title, String body, NotificationEntity.NotificationType type) {
        this.title = title;
        this.body = body;
        this.type = type;
    }
}
