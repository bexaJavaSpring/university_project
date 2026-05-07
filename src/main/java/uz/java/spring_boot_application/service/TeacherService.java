package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.grade.GradeDto;
import uz.java.spring_boot_application.dto.homework.HomeworkGradeRequestDto;
import uz.java.spring_boot_application.dto.student.StudentHomeworkResponse;
import uz.java.spring_boot_application.dto.user.TeacherFilter;
import uz.java.spring_boot_application.dto.user.TeacherRequest;
import uz.java.spring_boot_application.dto.user.TeacherResponse;
import uz.java.spring_boot_application.entities.*;
import uz.java.spring_boot_application.exception.CustomAccessDeniedException;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.StudentMapper;
import uz.java.spring_boot_application.mapper.TeacherMapper;
import uz.java.spring_boot_application.repository.*;
import uz.java.spring_boot_application.security.CustomUserDetails;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final FacultyRepository facultyRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final SubjectRepository subjectRepository;
    private final CacheManagerService cacheManagerService;
    private final UserSession userSession;
    private final StudentRepository studentRepository;
    private final StudentHomeworkRepository studentHomeworkRepository;
    private final HomeworkRepository homeworkRepository;
    private final StudentMapper studentMapper;
    private final HomeworkGradeSheetRepository homeworkGradeSheetRepository;

    @Transactional(readOnly = true)
    public TeacherResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.TEACHER);
        if (data != null) {
            return (TeacherResponse) data;
        }
        Teacher teacher = teacherRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("teacher.not.found")
        );
        CustomUserDetails currentUser = userSession.getCurrentUser();
        TeacherResponse response = teacherMapper.toResponse(teacher);
        cacheManagerService.put(id.toString(), CachePrefix.TEACHER, response);
        return response;

    }

    @Transactional(readOnly = true)
    public DataDto<List<TeacherResponse>> getAll(TeacherFilter filter) {
        Object data = cacheManagerService.get(filter.hashCode() + "", CachePrefix.TEACHER);
        if (data != null) {
            return (DataDto<List<TeacherResponse>>) data;
        }
        int page = filter.page() != null ? filter.page() : 0;
        int limit = filter.limit() != null ? filter.limit() : 10;
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by(filter.sortBy() != null ? filter.sortBy() : "createdAt").descending()
        );
        List<Teacher> all = teacherRepository.findAllCustomWithPagination(filter.salary(),
                filter.facultyId(),
                filter.subjectId(),
                pageRequest);
        List<TeacherResponse> response = all.stream().map(teacherMapper::toResponse).toList();
        cacheManagerService.put(filter.hashCode() + "", CachePrefix.TEACHER, new DataDto<>(response));
        return new DataDto<>(response);
    }

    @Transactional
    public Long create(TeacherRequest teacherRequest) {
        Faculty faculty = facultyRepository.findById(teacherRequest.getFacultyId()).orElse(null);
        if (faculty == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found");
        Subjects subject = subjectRepository.findById(teacherRequest.getSubjectId()).orElse(null);
        if (subject == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        Teacher teacher = teacherMapper.toEntity(teacherRequest);
        teacherRepository.save(teacher);
        cacheManagerService.delete(CachePrefix.TEACHER);
        return teacher.getId();
    }

    @Transactional
    public Long update(TeacherRequest teacherRequest, Long teacherId) {
        var teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new RuntimeException("Teacher not found")
        );
        teacherMapper.updateFromRequest(teacherRequest, teacher);
        if (teacherRequest.getFacultyId() != null) {
            facultyRepository.findById(teacherRequest.getFacultyId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found")
            );
        }
        if (teacherRequest.getSubjectId() != null) {
            subjectRepository.findById(teacherRequest.getSubjectId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found")
            );
        }
        teacherRepository.save(teacher);
        cacheManagerService.delete(CachePrefix.TEACHER);
        return teacherId;
    }

    @Transactional
    public Boolean delete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found")
        );
        teacher.markAsDeleted();
        teacherRepository.save(teacher);
        cacheManagerService.delete(CachePrefix.TEACHER);
        return true;
    }

    @Transactional(readOnly = true)
    public StudentHomeworkResponse viewStudentHomework(Long studentId, Long homeworkId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );

        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(
                () -> new GenericNotFoundException("homework.not.found")
        );
        StudentHomework studentHomework = studentHomeworkRepository.findByStudentIdAndHomeworkId(student.getId(), homework.getId());
        return studentMapper.toStudentHomeworkResponse(studentHomework);
    }

    @Transactional(readOnly = true)
    public List<StudentHomeworkResponse> getAllStudentHomeworks() {
        List<StudentHomework> all = studentHomeworkRepository.findAll();
        return all.stream().map(studentMapper::toStudentHomeworkResponse).toList();
    }

    public Long createHomeworkGrade(Long homeworkId, HomeworkGradeRequestDto dto) {
        Homework homework = homeworkRepository.findById(homeworkId).
                orElseThrow(()->new GenericNotFoundException("homework.not.found"));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId()).
                orElseThrow(()->new GenericNotFoundException("teacher.not.found"));
        CustomUserDetails customUserDetails = userSession.getCurrentUser();
        User user = customUserDetails.getUser();
        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();

        if (roles.contains("ROLE_TEACHER")) {
            boolean hasAccess = teacher.getGroups().stream()
                    .anyMatch(n->n.getId().equals(homework.getGroupId()));
            if (!hasAccess) {
                throw new CustomAccessDeniedException("access denied");
            }
        }

        List<Long> studentIds = dto.getGradeList()
                .stream().map(GradeDto::getStudentId).toList();

        long count = studentRepository.countByIdIn(studentIds);

        if (count!=studentIds.size())
            throw new RuntimeException("student.not.found");

        HomeworkGradeSheet homeworkGradeSheet = new HomeworkGradeSheet();
        homeworkGradeSheet.setHomework(homework);
        homeworkGradeSheet.setGradeList(dto.getGradeList());
        homeworkGradeSheet.setTeacher(teacher);
        homeworkGradeSheetRepository.save(homeworkGradeSheet);
        return homeworkGradeSheet.getId();

    }
}
