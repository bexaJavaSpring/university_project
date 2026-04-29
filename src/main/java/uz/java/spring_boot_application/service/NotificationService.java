package uz.java.spring_boot_application.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.config.FcmAppConfig;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.notification.NotificationRequest;
import uz.java.spring_boot_application.entities.NotificationEntity;
import uz.java.spring_boot_application.repository.NotificationRepository;

import java.util.List;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserSession userSession;

    public NotificationService(NotificationRepository notificationRepository, UserSession userSession) {
        this.notificationRepository = notificationRepository;
        this.userSession = userSession;
    }


    public void sendNotification(String fcmToken) {
//        create(new NotificationRequest("Salom", "Salom", NotificationEntity.NotificationType.SINGLE));
        NotificationEntity notify = notificationRepository.findById(2L).orElseThrow();
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(FcmAppConfig.getNotification(notify))
                    .setWebpushConfig(WebpushConfig.builder()
                            .setNotification(WebpushNotification.builder()
                                    .setTitle(notify.getTitle())
                                    .setBody(notify.getBody())
                                    .build())
                            .build())
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent message: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Error sending push notification: " + e.getErrorCode(), e);
        }
    }

    public void saveToken(String token) {
        boolean exists = notificationRepository.findAll()
                .stream()
                .anyMatch(n -> token.equals(n.getToken()));

        if (!exists) {
            NotificationEntity entity = new NotificationEntity();
            entity.setToken(token);
            notificationRepository.save(entity);
            System.out.println("✅ Token saqlandi: " + token);
        }
    }

    public void sendNotificationToTopic(NotificationEntity notify, String topic) {
        try {
            Message message = Message
                    .builder()
                    .setAndroidConfig(FcmAppConfig.androidConfig(notify))
                    .setApnsConfig(FcmAppConfig.apnsConfig(notify))
                    .setWebpushConfig(FcmAppConfig.webPushConfig(notify))
                    .setFcmOptions(FcmAppConfig.fcmOptions())
                    .setTopic(topic)
                    .setNotification(FcmAppConfig.getNotification(notify))
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Error sending push notification: " + e.getErrorCode(), e);
        }
    }


    public void create(NotificationRequest request) {
        NotificationEntity entity = new NotificationEntity();
        entity.setBody(request.getBody());
        entity.setTitle(request.getTitle());
        entity.setStatus(NotificationEntity.NotificationStatus.SENT);
        entity.setTopic(request.getBody());
        entity.setType(request.getType());
        notificationRepository.save(entity);
    }

    public void sendToAll(String title, String body) {
        List<NotificationEntity> all = notificationRepository.findAll();

        for (NotificationEntity entity : all) {
            try {
                Message message = Message.builder()
                        .setToken(entity.getToken())
                        .setNotification(FcmAppConfig.getNotification(entity))
                        .setWebpushConfig(WebpushConfig.builder()
                                .setNotification(WebpushNotification.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build())
                                .build())
                        .build();

                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("✅ Yuborildi: " + response);

            } catch (FirebaseMessagingException e) {
                System.out.println("❌ Xato: " + e.getMessage());
            }
        }
    }
}
