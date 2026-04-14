package uz.java.spring_boot_application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.java.spring_boot_application.dto.submission.SubmissionRequestDto;
import uz.java.spring_boot_application.dto.submission.SubmissionResponseDto;
import uz.java.spring_boot_application.entities.Submission;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    @Mapping(source = "homeworkId", target = "homework.id")
    @Mapping(source = "fileId", target = "file.id")
    Submission toEntity(SubmissionRequestDto dto);

    @Mapping(source = "homeworkId", target = "homework.id")
    @Mapping(source = "fileId", target = "file.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSubmissionFromDto(SubmissionRequestDto dto, Submission submission);


    @Mapping(source = "homework.title",target = "homeworkTitle")
    @Mapping(source = "homework.file.id", target = "fileId")
    SubmissionResponseDto toResponse(Submission submission);
}
