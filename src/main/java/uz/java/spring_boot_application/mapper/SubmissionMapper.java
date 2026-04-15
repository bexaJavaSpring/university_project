package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.submission.SubmissionRequestDto;
import uz.java.spring_boot_application.dto.submission.SubmissionResponseDto;
import uz.java.spring_boot_application.entities.Submission;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    @Mapping(source = "homeworkId", target = "homework.id")
    @Mapping(source = "fileId", target = "file.id")
    Submission toEntity(SubmissionRequestDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSubmissionFromDto(SubmissionRequestDto dto, @MappingTarget Submission submission);


    @Mapping(source = "homework.title",target = "homeworkTitle")
    @Mapping(source = "homework.file.id", target = "fileId")
    SubmissionResponseDto toResponse(Submission submission);
}
