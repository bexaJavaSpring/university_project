package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkResponseDto;
import uz.java.spring_boot_application.entities.*;
import uz.java.spring_boot_application.exception.CustomAccessDeniedException;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.HomeworkMapper;
import uz.java.spring_boot_application.repository.HomeworkRepository;
import uz.java.spring_boot_application.repository.TeacherRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;
    private final UserSession userSession;
    private final TeacherRepository teacherRepository;
    @Transactional
    public Long create(HomeworkRequestDto dto) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findBYUsername(user.getUsername());

        }
        Homework homework =homeworkMapper.toEntity(dto);
        return homeworkRepository.save(homework).getId();
    }
    @Transactional
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
    @Transactional
    public boolean delete(Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        homework.markAsDeleted();
        homeworkRepository.save(homework);
        return true;
    }
}
