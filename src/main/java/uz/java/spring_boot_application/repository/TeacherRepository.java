package uz.java.spring_boot_application.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.java.spring_boot_application.entities.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("""
        SELECT t FROM Teacher t
        WHERE (:salary IS NULL OR t.salary = :salary)
          AND (:facultyId IS NULL OR t.faculty.id = :facultyId)
          AND (:subjectId IS NULL OR t.subjects.id= :subjectId)
        """)
    List<Teacher> findAllCustomWithPagination(@Param("salary") Double salary,
                                              @Param("facultyId") Long facultyId,
                                              @Param("subjectId") Long subjectId,
                                              Pageable pageable);

    Teacher findByUsername(String username);


}

