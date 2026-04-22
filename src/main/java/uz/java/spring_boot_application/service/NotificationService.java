package uz.java.spring_boot_application.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.config.FcmAppConfig;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.notification.NotificationRequest;
import uz.java.spring_boot_application.entities.NotificationEntity;
import uz.java.spring_boot_application.repository.NotificationRepository;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {

    private final FirebaseMessaging firebaseMessagingQonun;
    private final NotificationRepository notificationRepository;
    private final UserSession userSession;

    public NotificationService(
            @Qualifier("university-app-firebase") FirebaseMessaging firebaseMessagingQonun, NotificationRepository notificationRepository, UserSession userSession) {
        this.firebaseMessagingQonun = firebaseMessagingQonun;
        this.notificationRepository = notificationRepository;
        this.userSession = userSession;
    }


    public void sendNotification(String fcmToken) {
        create(new NotificationRequest("Salom", "Salom", NotificationEntity.NotificationType.SINGLE));
        NotificationEntity notify = notificationRepository.findById(2L).orElseThrow();
        try {
            Message.Builder messageBuilder = Message
                    .builder()
                    .setAndroidConfig(FcmAppConfig.androidConfig(notify))
                    .setApnsConfig(FcmAppConfig.apnsConfig(notify))
                    .setWebpushConfig(FcmAppConfig.webPushConfig(notify))
                    .setToken(fcmToken);

            messageBuilder.setNotification(FcmAppConfig.getNotification(notify));
            firebaseMessagingQonun.send(messageBuilder.build());
            MulticastMessage.builder()
                    .setAndroidConfig(FcmAppConfig.androidConfig(notify))
                    .putAllData(Map.of("title", notify.getTitle()))
                    .addAllTokens(List.of(fcmToken))
                    .build();
            FirebaseMessaging.getInstance(
                    FirebaseApp.getInstance("university-app")
            ).sendMulticast(
                    MulticastMessage.builder()
                            .setAndroidConfig(FcmAppConfig.androidConfig(notify))
                            .putAllData(Map.of("title", notify.getTitle()))
                            .addAllTokens(List.of(fcmToken))
                            .build()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Error sending push notification: " + e.getErrorCode(), e);
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

            firebaseMessagingQonun.send(message);
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

}
