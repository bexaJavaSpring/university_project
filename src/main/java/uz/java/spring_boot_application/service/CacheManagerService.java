package uz.java.spring_boot_application.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.cache.CacheDto;
import uz.java.spring_boot_application.entities.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheManagerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserSession userSession;
    private ValueOperations<String, Object> operations;

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
        User user = userSession.getCurrentUser().getUser();
        return String.format("%s/%s/%s", cachePrefix, key, user.getId());
    }

    public void delete(String cachePrefix) {
        Set<String> allKeys = redisTemplate.keys("*");
        if (!allKeys.isEmpty()) {
            redisTemplate.delete(allKeys.stream()
                    .filter(deletedKey -> deletedKey.startsWith(cachePrefix))
                    .collect(Collectors.toSet()));
        }
    }

    public void deleteMultiple(List<String> cachePrefixes) {
        User user = userSession.getCurrentUser().getUser();
        Set<String> allKeys = redisTemplate.keys("*");
        for (String cachePrefix : cachePrefixes) {
            if (!allKeys.isEmpty()) {
                redisTemplate.delete(allKeys.stream()
                        .filter(deletedKey -> deletedKey.startsWith(cachePrefix) &&
                                (deletedKey.contains(user.getId().toString())))
                        .collect(Collectors.toSet()));
            }
        }
    }
}
