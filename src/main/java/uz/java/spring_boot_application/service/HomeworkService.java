package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.homework.HomeworkCreateRequestDto;
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
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;
import uz.java.spring_boot_application.specification.HomeworkSpecification;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserSession userSession;
    private final CacheManagerService cacheManagerService;
    private final UserRepository userRepository;

    @Transactional
    public Long create(HomeworkCreateRequestDto dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId()).
                orElseThrow(()->new GenericNotFoundException("Teacher not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // 🔥 role JWT dan
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");

        if (roles.contains("TEACHER")) {
            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(dto.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }

        }
        Homework homework = homeworkMapper.toEntityCreate(dto);
        homework.setTeacher(teacher);
        Long id = homeworkRepository.save(homework).getId();
        cacheManagerService.delete(CachePrefix.HOMEWORK);
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(Long id, HomeworkRequestDto dto) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new GenericNotFoundException("user not found"));
        // 🔥 role JWT dan
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        boolean isSuperAdmin = roles.contains("SUPER_ADMIN");
        boolean isTeacher = roles.contains("TEACHER");
        Teacher teacher =null;
        if (isTeacher) {
            teacher = teacherRepository.findById(user.getId())
                    .orElseThrow(()-> new GenericNotFoundException("teacher not found"));
        }

        if (!isSuperAdmin) {
            if (!isTeacher)
                throw new CustomAccessDeniedException("access denied");

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(dto.getGroupId()));

            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }

        }
        homeworkMapper.updateHomeworkFromDto(dto, homework);
       Homework homework1= homeworkRepository.save(homework);

//        cacheManagerService.delete(CachePrefix.HOMEWORK);
        return homework1.getId();
    }

    @Transactional(readOnly = true)
    public HomeworkResponseDto getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.HOMEWORK);
        if (data!=null){
            return (HomeworkResponseDto) data;
        }
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(()->new GenericNotFoundException("homework not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> realmAccess= jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        String username = jwt.getClaimAsString("preferred_username");
        if (roles.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUsername(username);

            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(homework.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        } else if (roles.contains("ROLE_STUDENT")) {
            Student student = studentRepository.findByUsername(username);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> realmAccess= jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        String username = jwt.getClaimAsString("preferred_username");
        if (roles.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUsername(username);

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
        HomeworkSpecification homeworkSpecification = new HomeworkSpecification(homeworkFilter);

        List<Homework> homeworkList = homeworkRepository.findAll(homeworkSpecification, SearchSpecification.getPageable(
                homeworkFilter.page(),homeworkFilter.size(),homeworkFilter.sortBy()
        )).toList();

        List<HomeworkResponseDto> responseDtoList = homeworkList.stream().map(homeworkMapper::toResponseDto).toList();
        cacheManagerService.put(homeworkFilter.hashCode()+"", CachePrefix.HOMEWORK, new DataDto<>(responseDtoList));
        return new DataDto<>(responseDtoList);
    }
}
