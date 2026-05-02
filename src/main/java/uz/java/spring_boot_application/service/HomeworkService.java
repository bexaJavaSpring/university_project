package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.homework.HomeworkFilter;
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
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserSession userSession;
    private final CacheManagerService cacheManagerService;

    @Transactional
    public Long create(HomeworkRequestDto dto) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        Teacher teacher=null;
        if (list.contains("ROLE_TEACHER")) {
             teacher = teacherRepository.findByUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(dto.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }

        }
        Homework homework = homeworkMapper.toEntity(dto);
        homework.setTeacher(teacher);
        Long id = homeworkRepository.save(homework).getId();
        cacheManagerService.delete(CachePrefix.HOMEWORK);
        return id;
    }

    @Transactional
    public Long update(Long id, HomeworkRequestDto dto) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        Teacher teacher=null;
        if (list.contains("ROLE_TEACHER")) {
             teacher = teacherRepository.findByUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(dto.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }

        }
        homeworkMapper.updateHomeworkFromDto(dto, homework);
        homework.setTeacher(teacher);
        homeworkRepository.save(homework).getId();
        cacheManagerService.delete(CachePrefix.HOMEWORK);
        return id;
    }

    @Transactional(readOnly = true)
    public HomeworkResponseDto getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.HOMEWORK);
        if (data!=null){
            return (HomeworkResponseDto) data;
        }
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(homework.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        } else if (list.contains("ROLE_STUDENT")) {
            Student student = studentRepository.findByUsername(user.getUsername());
            boolean hasAccess = homework.getGroupId().equals(student.getGroup().getId());
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        }

        HomeworkResponseDto responseDto = homeworkMapper.toResponseDto(homework);
        cacheManagerService.put(id.toString(),CachePrefix.HOMEWORK,responseDto);
        return responseDto;
    }
    @Transactional
    public boolean delete(Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUsername(user.getUsername());

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(homework.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        }
        homework.markAsDeleted();
        homeworkRepository.save(homework);
        cacheManagerService.delete(CachePrefix.HOMEWORK);
        return true;
    }


    @Transactional(readOnly = true)
    public DataDto<List<HomeworkResponseDto>> getAll(HomeworkFilter homeworkFilter) {
        Object data = cacheManagerService.get(homeworkFilter.hashCode() + "", CachePrefix.HOMEWORK);
        if (data!=null){
            return (DataDto<List<HomeworkResponseDto>>) data;
        }
        PageRequest pageRequest = PageRequest.of(homeworkFilter.page(), homeworkFilter.size(), Sort.by(homeworkFilter.sortBy()!=null?homeworkFilter.sortBy():"id").ascending());
        Page<Homework> page = homeworkRepository.findByAllHomework(homeworkFilter.title(),homeworkFilter.groupId(),pageRequest);
        List<HomeworkResponseDto>  responseDtoList = page.stream().map(homeworkMapper::toResponseDto).toList();
        cacheManagerService.put(homeworkFilter.hashCode()+"", CachePrefix.HOMEWORK, new DataDto<>(responseDtoList));
        return new DataDto<>(responseDtoList);
    }
}
