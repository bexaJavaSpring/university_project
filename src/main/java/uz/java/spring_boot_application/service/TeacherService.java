package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uz.java.spring_boot_application.dto.user.TeacherFilter;
import uz.java.spring_boot_application.dto.user.TeacherRequest;
import uz.java.spring_boot_application.dto.user.TeacherResponse;
import uz.java.spring_boot_application.entities.Faculty;
import uz.java.spring_boot_application.entities.Subjects;
import uz.java.spring_boot_application.entities.Teacher;
import uz.java.spring_boot_application.mapper.TeacherMapper;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.SubjectRepository;
import uz.java.spring_boot_application.repository.TeacherRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final FacultyRepository facultyRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final SubjectRepository subjectRepository;


    public List<TeacherResponse> getAll(TeacherFilter filter) {
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
        return all.stream().map(teacherMapper::toResponse).toList();
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
        return teacherRepository.save(teacher).getId();
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
        return teacherId;
    }
    @Transactional
    public Boolean delete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found")
        );
        teacher.markAsDeleted();
        teacherRepository.save(teacher);

        return true;
    }
}
