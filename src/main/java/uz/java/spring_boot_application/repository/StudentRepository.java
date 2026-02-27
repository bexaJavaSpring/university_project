package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
