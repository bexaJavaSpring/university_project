package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.Group;

public interface GroupRepository extends JpaRepository<Group,Long> {
}
