package uz.java.spring_boot_application.mapper;

import org.mapstruct.*;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkResponseDto;
import uz.java.spring_boot_application.entities.Homework;

@Mapper(componentModel = "spring")
public interface HomeworkMapper {

    Homework toEntity(HomeworkRequestDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHomeworkFromDto(HomeworkRequestDto dto, @MappingTarget Homework entity);

    HomeworkResponseDto toResponseDto(Homework homework);
}
