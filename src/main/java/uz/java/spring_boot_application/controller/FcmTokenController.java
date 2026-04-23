package uz.java.spring_boot_application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.java.spring_boot_application.service.NotificationService;

import java.util.Map;

@RestController
@RequestMapping("/fcmtoken")
public class FcmTokenController {

    private final NotificationService notificationService;

    public FcmTokenController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/save-token")
    public ResponseEntity<String> saveToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        notificationService.sendNotification(token);
        return ResponseEntity.ok("OK");
    }
}
