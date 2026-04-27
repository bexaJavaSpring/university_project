package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.group.GroupRequest;
import uz.java.spring_boot_application.dto.group.GroupResponse;
import uz.java.spring_boot_application.entities.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponse toResponse(Group group);

    @Mapping(source = "facultyId", target = "faculty.id")
    Group toEntity(GroupRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(GroupRequest request, @MappingTarget Group group);
}
