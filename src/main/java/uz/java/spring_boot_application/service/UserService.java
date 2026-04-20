package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.user.UserFilter;
import uz.java.spring_boot_application.dto.user.UserRequest;
import uz.java.spring_boot_application.dto.user.UserResponse;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.UserMapper;
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.specification.UserSpecification;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CacheManagerService cacheManagerService;

    @Transactional(readOnly = true)
    public DataDto<List<UserResponse>> getAll(UserFilter userFilter) {
        String key = "USERS_KEY";
        Object data = cacheManagerService.get(key, CachePrefix.USER);
        if (data !=null){
            return (DataDto<List<UserResponse>>) data;
        }
        UserSpecification specification = new UserSpecification(userFilter);
        List<User> all = userRepository.findAll(specification, SearchSpecification.getPageable(
                userFilter.page(),userFilter.size(),userFilter.sortBy())).toList();

        List<UserResponse> list = all.stream().map(userMapper::toResponse).toList();
        cacheManagerService.put(key, CachePrefix.USER, new DataDto<>(list));
        return new DataDto<>(list);
    }

    @Transactional(readOnly = true)
    public UserResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.USER);
        if (data!=null){
            return (UserResponse) data;
        }
        User user = userRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("user.not.found")
        );
        UserResponse response = userMapper.toResponse(user);
        cacheManagerService.put(id.toString(), CachePrefix.USER, response);
        return response;
    }
    @Transactional
    public Long create(UserRequest request) {
        User entity = userMapper.toEntity(request);
        userRepository.save(entity);
        cacheManagerService.delete(CachePrefix.USER);
        return entity.getId();
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
