package uz.java.spring_boot_application.config;

import com.google.firebase.messaging.*;
import uz.java.spring_boot_application.entities.NotificationEntity;

import java.util.Map;

public interface FcmAppConfig {
    // ✅ Android uchun
    static AndroidConfig androidConfig(NotificationEntity notify) {
        return AndroidConfig
                .builder()
                .setTtl(3600 * 1000)
                .setPriority(AndroidConfig.Priority.HIGH)
                .putAllData(Map.of("body", notify.getBody(), "title", notify.getTitle()))
                .setNotification(AndroidNotification
                        .builder()
                        .setTitle(notify.getTitle())
                        .setBody(notify.getBody())
                        .setSound("default")
                        .build())
                .setFcmOptions(AndroidFcmOptions
                        .builder()
                        .setAnalyticsLabel("android-university-app")
                        .build())
                .build();
    }

    // ✅ iOS (Apple) uchun
    static ApnsConfig apnsConfig(NotificationEntity notify) {
        return ApnsConfig
                .builder()
                .setAps(Aps
                        .builder()
                        .setBadge(1)
                        .setSound("default")
                        .setAlert(ApsAlert
                                .builder()
                                .setTitle(notify.getTitle())
                                .setBody(notify.getBody())
                                .build())
                        .build())
                .setFcmOptions(ApnsFcmOptions
                        .builder()
                        .setAnalyticsLabel("university-app")
                        .build())
                .build();
    }

    // ✅ Web (Browser) uchun
    static WebpushConfig webPushConfig(NotificationEntity notify) {
        return WebpushConfig
                .builder()
                .setNotification(WebpushNotification
                        .builder()
                        .setTitle(notify.getTitle())
                        .setBody(notify.getBody())
                        .setBadge("1")
                        .build())
                .build();
    }

    // ✅ Notification object
    static Notification getNotification(NotificationEntity notf) {
        Notification notification = new Notification(
                notf.getTitle(), notf.getBody()
        );
        return notification;
    }

    // ✅ FCM Options
    static FcmOptions fcmOptions() {
        return FcmOptions
                .builder()
                .setAnalyticsLabel("university-app")
                .build();
    }
}
