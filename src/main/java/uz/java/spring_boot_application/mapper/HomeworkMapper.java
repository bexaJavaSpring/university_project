package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkResponseDto;
import uz.java.spring_boot_application.entities.Homework;

@Mapper(componentModel = "spring")
public interface HomeworkMapper {

    @Mapping(source = "fileId", target = "file.id")
    @Mapping(source = "groupId",target = "group.id")
    Homework toEntity(HomeworkRequestDto dto);

    @Mapping(source = "fileId", target = "file.id")
    @Mapping(source = "groupId",target = "group.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHomeworkFromDto(HomeworkRequestDto dto, @MappingTarget Homework entity);

    @Mapping(source = "file.id", target = "fileId")
    HomeworkResponseDto toResponseDto(Homework homework);
}
