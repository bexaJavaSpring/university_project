package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
