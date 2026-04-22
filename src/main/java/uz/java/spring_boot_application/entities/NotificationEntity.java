package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity extends Auditable {

    @Column(name = "title")
    String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    private String imageUrl;

    private String userId;

    @Column(length = 500)
    private String token;

    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    private String messageId;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Integer successCount;

    private Integer failureCount;

    public enum NotificationType {
        SINGLE,      // Bitta token
        MULTICAST,   // Ko'p token
        TOPIC,       // Topic
        BROADCAST    // Barcha
    }

    public enum NotificationStatus {
        PENDING,     // Yuborilmagan (queue da)
        SENT,        // Muvaffaqiyatli yuborildi
        PARTIAL,     // Qisman yuborildi (multicast da)
        FAILED       // Butunlay xato
    }
}
