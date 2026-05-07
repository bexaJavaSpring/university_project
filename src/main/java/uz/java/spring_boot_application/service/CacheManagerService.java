package uz.java.spring_boot_application.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.cache.CacheDto;
import uz.java.spring_boot_application.entities.Role;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheManagerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserSession userSession;
    private ValueOperations<String, Object> operations;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        this.operations = redisTemplate.opsForValue();
    }

    public Object get(String key, String cachePrefix) {
        return operations.get(generateKey(key, cachePrefix));
    }

    public void put(String key, String cachePrefix, Object data) {
        operations.set(generateKey(key, cachePrefix), data);
    }

    public void putData(String key, String cachePrefix, CacheDto data) {
        operations.set(generateKey(key, cachePrefix), data);
    }

    private String generateKey(String key, String cachePrefix) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();
        User user = userRepository.findByKeycloakUserId(keycloakId)
                .orElseThrow(() -> new GenericNotFoundException("User not found"));
        return String.format("%s/%s/%s", cachePrefix, key, user.getId());
    }

    public void delete(String cachePrefix) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            return;
        }

        // 🔹 username
        String username = jwt.getClaim("preferred_username");

        // 🔹 role JWT dan
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");

        // 🔹 user DB dan (id olish uchun)
        Long userId = userRepository.findIdByUsername(username);

        Set<String> allKeys = redisTemplate.keys("*");
        if (roles.containsAll(List.of("ROLE_SUPERADMIN", "ROLE_REKTOR"))) {
            if (!allKeys.isEmpty()) {
                redisTemplate.delete(allKeys.stream()
                        .filter(deletedKey -> deletedKey.startsWith(cachePrefix))
                        .collect(Collectors.toSet()));
            }
        } else {
            if (!allKeys.isEmpty()) {
                redisTemplate.delete(allKeys.stream()
                        .filter(deletedKey -> deletedKey.startsWith(cachePrefix)
                                && deletedKey.endsWith(userId.toString()))
                        .collect(Collectors.toSet()));
            }
        }
    }

    public void deleteMultiple(List<String> cachePrefixes) {
        User user = userSession.getCurrentUser().getUser();
        Set<String> set = user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet());
        Set<String> allKeys = redisTemplate.keys("*");
        for (String cachePrefix : cachePrefixes) {
            if (set.containsAll(List.of("ROLE_SUPERADMIN", "ROLE_REKTOR"))) {
                if (!allKeys.isEmpty()) {
                    redisTemplate.delete(allKeys.stream()
                            .filter(deletedKey -> deletedKey.startsWith(cachePrefix))
                            .collect(Collectors.toSet()));
                }
            } else {
                if (!allKeys.isEmpty()) {
                    redisTemplate.delete(allKeys.stream()
                            .filter(deletedKey -> deletedKey.startsWith(cachePrefix)
                                    && deletedKey.endsWith(user.getId().toString()))
                            .collect(Collectors.toSet()));
                }
            }
        }
    }
}
