package uz.java.spring_boot_application.config;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import uz.java.spring_boot_application.entities.User;

import uz.java.spring_boot_application.repository.UserRepository;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditableConfig {

//    @Bean(name = "auditorAware")
//    public AuditorAware<Long> auditorAware() {
//        return new AuditAwareImpl();
//    }
}

@Component("auditorAware")
@RequiredArgsConstructor
class AuditAwareImpl implements AuditorAware<Long> {
    private final UserRepository userRepository;

    @Override
    @Nonnull
    public Optional<Long> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated())
                return Optional.empty();

            if (authentication.getPrincipal() instanceof Jwt jwt) {
                String keycloakId = jwt.getSubject(); // "sub" claim — Keycloak UUID

                return userRepository.findByKeycloakUserId(keycloakId)
                        .map(User::getId);
            }

            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
