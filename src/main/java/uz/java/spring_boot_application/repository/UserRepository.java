package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
