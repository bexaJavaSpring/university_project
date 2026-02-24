package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.University;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
