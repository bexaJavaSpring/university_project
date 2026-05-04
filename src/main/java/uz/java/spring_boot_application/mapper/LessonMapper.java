package uz.java.spring_boot_application.mapper;

import com.google.common.io.LittleEndianDataOutputStream;
import org.mapstruct.*;
import uz.java.spring_boot_application.dto.faculty.FacultyRequest;
import uz.java.spring_boot_application.dto.lesson.LessonRequest;
import uz.java.spring_boot_application.dto.lesson.LessonResponse;
import uz.java.spring_boot_application.entities.Faculty;
import uz.java.spring_boot_application.entities.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonResponse toResponse(Lesson lesson);

    @Mapping(source = "subjectId", target = "subjects.id")
    Lesson toEntity(LessonRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(LessonRequest request, @MappingTarget Lesson lesson);
}
