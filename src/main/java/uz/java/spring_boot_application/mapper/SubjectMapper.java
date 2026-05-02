package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.subject.SubjectRequest;
import uz.java.spring_boot_application.dto.subject.SubjectResponse;
import uz.java.spring_boot_application.entities.Subjects;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mapping(source = "name", target = "subjectName")
    @Mapping(source = "description", target = "description")
    SubjectResponse toResponse(Subjects subjects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(SubjectRequest subjectRequest, @MappingTarget Subjects subject);

    Subjects toEntity(SubjectRequest subjectRequest);
}
