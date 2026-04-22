//package uz.java.spring_boot_application.config;
//
//import com.google.firebase.messaging.*;
//import uz.java.spring_boot_application.entities.NotificationEntity;
//
//import java.util.Map;
//
//public interface FcmAppConfig {
//    static AndroidConfig androidConfig(NotificationEntity notify) {
//
//        String body = notify.getBody();
//
//        return AndroidConfig
//                .builder()
//                .setTtl(3600 * 1000)
//                .putAllData(Map.of("data", body))
//                .setFcmOptions(AndroidFcmOptions
//                        .builder()
//                        .setAnalyticsLabel("android-university-app")
//                        .build())
//                .build();
//    }
//
//    static ApnsConfig apnsConfig(NotificationEntity notify) {
//        return ApnsConfig
//                .builder()
//                .setAps(Aps
//                        .builder()
//                        .setBadge(1)
//                        .putAllCustomData(Map.of("data", notify.getBody()))
//                        .build())
//                .setFcmOptions(ApnsFcmOptions
//                        .builder()
//                        .setAnalyticsLabel("university-app")
//                        .build())
//                .build();
//    }
//
//    static WebpushConfig webPushConfig(NotificationEntity notify) {
//        return WebpushConfig
//                .builder()
//                .putAllData(Map.of("data", notify.getBody()))
//                .build();
//    }
//
//    static Notification getNotification(NotificationEntity notf) {
//        Notification notification = new Notification(
//                notf.getTitle(), notf.getBody()
//        );
//        return notification;
//    }
//
//    static FcmOptions fcmOptions() {
//        return FcmOptions
//                .builder()
//                .setAnalyticsLabel("university-app")
//                .build();
//    }
//}
