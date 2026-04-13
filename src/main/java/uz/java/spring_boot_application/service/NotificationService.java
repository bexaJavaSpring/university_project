package uz.java.spring_boot_application.service;

import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.exception.GenericRuntimeException;

@Service
public class NotificationService {

    public void sendNotification(String message) {
        // telegram bot, email, fiebase orqali message yuborish
//        2-tomon ga yuboradi(web app, mobile app, desktop app) message va notf lani yuboradi
        if (message != null)
            throw new GenericRuntimeException("Message is not null");
        System.out.println(message);
    }
}
