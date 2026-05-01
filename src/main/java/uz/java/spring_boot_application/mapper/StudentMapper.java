package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.student.StudentHomeworkResponse;
import uz.java.spring_boot_application.dto.student.StudentRequest;
import uz.java.spring_boot_application.dto.student.StudentResponse;
import uz.java.spring_boot_application.dto.student.SubmitHomeworkRequest;
import uz.java.spring_boot_application.entities.Student;
import uz.java.spring_boot_application.entities.StudentHomework;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "group.id", target = "groupId")
    StudentResponse toResponse(Student student);

    Student toEntity(StudentRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(StudentRequest request, @MappingTarget Student student);

    @Mapping(source = "homeworkId", target = "homework.id")
    @Mapping(source = "studentId", target = "student.id")
    StudentHomework toStudentHomework(SubmitHomeworkRequest request);

    @Mapping(target = "homeworkId", source = "homework.id")
    @Mapping(target = "studentId", source = "student.id")
    StudentHomeworkResponse toStudentHomeworkResponse(StudentHomework studentHomework);
}
