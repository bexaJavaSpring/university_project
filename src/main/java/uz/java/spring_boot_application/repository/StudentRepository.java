package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.spring_boot_application.entities.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsername(String username);

    long countByIdIn(List<Long> studentIds);
}
