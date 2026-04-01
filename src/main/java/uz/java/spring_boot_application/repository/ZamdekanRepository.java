package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.java.spring_boot_application.entities.Zamdekan;

public interface ZamdekanRepository extends JpaRepository<Zamdekan, Long> {
    @Query("select t from Zamdekan t where t.faculty.id=?1")
    Zamdekan findFacultyId(Long facultyId);

    Zamdekan findByUsername(String username);
}
