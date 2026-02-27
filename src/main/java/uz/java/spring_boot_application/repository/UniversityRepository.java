package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.java.spring_boot_application.entities.University;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {
//    @Query(value = "select * from universities u where u.is_deleted = false ", nativeQuery = true) -> bu ozimizni postgresql dagi query
    @Query("select t from University t where t.deleted = false ")  // bu HQL(Hibernate Query Language)
    List<University> findAllCustom();
}
