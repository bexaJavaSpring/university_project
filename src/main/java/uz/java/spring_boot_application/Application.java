package uz.java.spring_boot_application;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.java.spring_boot_application.entities.Role;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.repository.RoleRepository;
import uz.java.spring_boot_application.repository.UserRepository;

import java.util.Set;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableCaching
public class Application {
//    private final PasswordEncoder passwordEncoder;
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//
//    public Application(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//    }
//
//        @PostConstruct
//    public void createSuperAdmin() {
//        User user = new User();
//        user.setFirstName("Super Admin");
//        user.setUsername("superadmin");
//        user.setPassword(passwordEncoder.encode("superAdmin@1234"));
//        user.setEmail("superadmin@gmail.com");
//        Role role = roleRepository.findByCode("ROLE_SUPER_ADMIN");
//        if (role == null) {
//            role = new Role();
//            role.setCode("ROLE_SUPER_ADMIN");
//            role.setName("Super Admin");
//            roleRepository.save(role);
//        }
//        user.setRoles(Set.of(role));
//        userRepository.save(user);
//        log.info("Super Admin created");
//    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
