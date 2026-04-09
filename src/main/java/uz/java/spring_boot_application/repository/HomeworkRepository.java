package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.java.spring_boot_application.entities.Homework;
@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
}
