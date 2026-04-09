package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkResponseDto;
import uz.java.spring_boot_application.entities.Homework;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.HomeworkMapper;
import uz.java.spring_boot_application.repository.HomeworkRepository;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;

    public Long create(HomeworkRequestDto dto) {
        Homework homework =homeworkMapper.toEntity(dto);
        return homeworkRepository.save(homework).getId();
    }

    public Long update(Long id, HomeworkRequestDto dto) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        homeworkMapper.updateHomeworkFromDto(dto, homework);
        return homeworkRepository.save(homework).getId();
    }

    public HomeworkResponseDto getOne(Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        return homeworkMapper.toResponseDto(homework);
    }

    public boolean delete(Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        homework.markAsDeleted();
        homeworkRepository.save(homework);
        return true;
    }
}
