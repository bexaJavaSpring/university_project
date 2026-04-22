package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.java.spring_boot_application.entities.SessionUser;

public interface SessionUserRepository extends JpaRepository<SessionUser, Long> {
    @Query("select s from SessionUser s join User u on s.user.id=u.id where u.id=:userId")
    SessionUser findByUserId(@Param("userId") Long userId);
}
