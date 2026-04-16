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
import uz.java.spring_boot_application.repository.StudentRepository;
import uz.java.spring_boot_application.repository.TeacherRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserSession userSession;

    @Transactional
    public Long create(HomeworkRequestDto dto) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findBYUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(dto.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }

        }
        Homework homework = homeworkMapper.toEntity(dto);
        return homeworkRepository.save(homework).getId();
    }

    @Transactional
    public Long update(Long id, HomeworkRequestDto dto) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findBYUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(dto.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }

        }
        homeworkMapper.updateHomeworkFromDto(dto, homework);
        return homeworkRepository.save(homework).getId();
    }

    @Transactional(readOnly = true)
    public HomeworkResponseDto getOne(Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findBYUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(homework.getGroup().getId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        } else if (list.contains("ROLE_STUDENT")) {
            Student student = studentRepository.findByUsername(user.getUsername());
            boolean hasAccess = homework.getGroup().getId().equals(student.getGroup().getId());
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        }
        return homeworkMapper.toResponseDto(homework);
    }
    @Transactional
    public boolean delete(Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findBYUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(homework.getGroup().getId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        }
        homework.markAsDeleted();
        homeworkRepository.save(homework);
        return true;
    }
}
