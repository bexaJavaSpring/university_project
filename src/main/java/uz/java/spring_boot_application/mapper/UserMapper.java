package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.user.UserRequest;
import uz.java.spring_boot_application.dto.user.UserResponse;
import uz.java.spring_boot_application.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserResponse toResponse(User user);

    @Mapping(target = "keycloakUserId", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(@MappingTarget User user, UserRequest request);
}
