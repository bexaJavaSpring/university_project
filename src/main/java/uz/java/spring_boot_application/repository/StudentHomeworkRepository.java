package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.java.spring_boot_application.entities.StudentHomework;

public interface StudentHomeworkRepository extends JpaRepository<StudentHomework, Long> {
    @Query("select t from StudentHomework t where t.student.id=?1 and t.homework.id=?2")
    StudentHomework findByStudentIdAndHomeworkId(Long studentId, Long homeworkId);
}
