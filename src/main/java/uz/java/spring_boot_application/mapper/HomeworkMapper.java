package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.homework.HomeworkCreateRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkResponseDto;
import uz.java.spring_boot_application.entities.Homework;

@Mapper(componentModel = "spring")
public interface HomeworkMapper {

    Homework toEntity(HomeworkRequestDto dto);
    Homework toEntityCreate(HomeworkCreateRequestDto dto);

    @Mapping(target = "teacher", ignore = true )
    @Mapping(target = "attachmentUrls", expression = "java(dto.getAttachmentUrls() != null ? new java.util.ArrayList<>(dto.getAttachmentUrls()) : new java.util.ArrayList<>())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHomeworkFromDto(HomeworkRequestDto dto, @MappingTarget Homework entity);

    HomeworkResponseDto toResponseDto(Homework homework);
}
