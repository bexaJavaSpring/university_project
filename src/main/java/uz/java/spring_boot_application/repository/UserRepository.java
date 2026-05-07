package uz.java.spring_boot_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<User> findByKeycloakUserId(String keycloakUserId);

    @Query("select u.id from User u where u.username= :username")
    Long findIdByUsername(@Param("username") String username);
}
