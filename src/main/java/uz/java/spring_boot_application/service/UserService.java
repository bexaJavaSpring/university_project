package uz.java.spring_boot_application.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.user.UserRequest;
import uz.java.spring_boot_application.dto.user.UserResponse;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.AlreadyExistsException;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.exception.GenericRuntimeException;
import uz.java.spring_boot_application.mapper.UserMapper;
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.Family.SUCCESSFUL;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CacheManagerService cacheManagerService;
    private final Keycloak keycloak;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Transactional(readOnly = true)
    public DataDto<List<UserResponse>> getAll() {
        String key = "USERS_KEY";
        cacheManagerService.get(key, CachePrefix.USER);
        List<User> all = userRepository.findAll();
        List<UserResponse> list = all.stream().map(userMapper::toResponse).toList();
        cacheManagerService.put(key, CachePrefix.USER, new DataDto<>(list));
        return new DataDto<>(list);
    }

    @Transactional(readOnly = true)
    public UserResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.USER);
        if (data != null) {
            return (UserResponse) data;
        }
        User user = userRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("user.not.found")
        );
        UserResponse response = userMapper.toResponse(user);
        cacheManagerService.put(id.toString(), CachePrefix.USER, response);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(UserRequest request) {
        // 1-chi Keycloak user yaratiladi undan keyin ozimizni bazada
        // 1-qism Keycloak ga saqlash qismi
        if (!checkUsername(request.getUsername())) {
            throw new AlreadyExistsException(request.getUsername());
        }
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setEnabled(true);
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setUsername(request.getUsername());
        // password saqlash
        var password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setTemporary(false);
        password.setValue(request.getPassword());
        userRepresentation.setCredentials(List.of(password));
        String keycloakUserId = null;
        try (Response response = keycloak.realm(realm)
                .users()
                .create(userRepresentation)) {

            if (response.getStatusInfo().getFamily() != SUCCESSFUL) {
                throw new GenericRuntimeException("keycloak.user.create.failed");
            }

            keycloakUserId = extractUserId(response);
            return saveEntity(request, keycloakUserId);

        } catch (Exception e) {
            // rollback Keycloak user (faqat yaratilgan bo‘lsa)
            if (keycloakUserId != null) {
                try {
                    keycloak.realm(realm)
                            .users()
                            .delete(keycloakUserId);
                } catch (Exception ex) {
                    log.error("Failed to rollback Keycloak user: {}", keycloakUserId, ex);
                }
            }
            throw new GenericRuntimeException("user.create.failure", e.getMessage());
        }
    }

    private String extractUserId(Response response) {
        String location = response.getLocation().toString();
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public Long saveEntity(UserRequest request, String keycloakUserId) {
        User entity = userMapper.toEntity(request);
        entity.setKeycloakUserId(keycloakUserId);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        User save = userRepository.save(entity);
//        cacheManagerService.delete(CachePrefix.USER);
        return save.getId();
    }

    private boolean checkUsername(String username) {
        return keycloak.realm(realm)
                .users()
                .searchByUsername(username, false).isEmpty();
    }

    @Transactional
    public Long update(Long userId, UserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new GenericNotFoundException("user.not.found")
        );
        userMapper.updateFromRequest(user, request);
        userRepository.save(user);
        cacheManagerService.delete(CachePrefix.USER);
        return user.getId();
    }

    @Transactional
    public Boolean delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("user.not.found")
        );
        user.markAsDeleted();
        userRepository.save(user);
        cacheManagerService.delete(CachePrefix.USER);
        return true;
    }
}
