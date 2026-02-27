package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.java.spring_boot_application.entities.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    @Query("select t from Faculty t where t.deleted = false ")
    List<Faculty> findAllCustom();
}
