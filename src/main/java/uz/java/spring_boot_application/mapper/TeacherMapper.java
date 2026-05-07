package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.user.TeacherRequest;
import uz.java.spring_boot_application.dto.user.TeacherResponse;
import uz.java.spring_boot_application.entities.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    @Mapping(source="subjects.id", target = "subjectId")
    @Mapping(source="faculty.id", target = "facultyId")
    @Mapping(source="phone", target = "userResponse.phone")
    @Mapping(source="firstName", target = "userResponse.firstName")
    @Mapping(source="lastName", target = "userResponse.lastName")
    @Mapping(source="birthDate", target = "userResponse.birthDate")
    @Mapping(source="gender", target = "userResponse.gender")
    TeacherResponse toResponse(Teacher teacher);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(TeacherRequest request, @MappingTarget Teacher teacher);

    @Mapping(source = "facultyId", target = "faculty.id")
    Teacher toEntity(TeacherRequest teacherRequest);
}
