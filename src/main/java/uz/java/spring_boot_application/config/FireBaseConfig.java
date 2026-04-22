//package uz.java.spring_boot_application.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//@Slf4j
//public class FireBaseConfig {
//
//    @Value("${firebase.config.file}")
//    private String firebaseConfigPath;
//
//    @Bean
//    @Qualifier("university-app-firebase")
//    public FirebaseMessaging universityFirebase() {
//        try {
//            // Load the classpath resource
//            Resource resource = new ClassPathResource(firebaseConfigPath.replace("classpath:", ""));
//
//            // Use try-with-resources to auto-close the stream
//            try (InputStream serviceAccount = resource.getInputStream()) {
//                GoogleCredentials googleCredentials = GoogleCredentials
//                        .fromStream(serviceAccount);
//                FirebaseOptions firebaseOptions = FirebaseOptions
//                        .builder()
//                        .setCredentials(googleCredentials)
//                        .build();
//                FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "university-app");
//                return FirebaseMessaging.getInstance(app);
//            }
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//
//        return null;
//    }
//}
