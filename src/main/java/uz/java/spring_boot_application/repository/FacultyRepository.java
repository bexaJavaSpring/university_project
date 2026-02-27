package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

}
