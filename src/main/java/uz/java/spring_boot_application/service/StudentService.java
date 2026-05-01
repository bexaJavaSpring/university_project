package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.student.StudentRequest;
import uz.java.spring_boot_application.dto.student.StudentResponse;
import uz.java.spring_boot_application.dto.student.SubmitHomeworkRequest;
import uz.java.spring_boot_application.entities.Student;
import uz.java.spring_boot_application.entities.StudentHomework;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.StudentMapper;
import uz.java.spring_boot_application.repository.HomeworkRepository;
import uz.java.spring_boot_application.repository.StudentHomeworkRepository;
import uz.java.spring_boot_application.repository.StudentRepository;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CacheManagerService cacheManagerService;
    private final HomeworkRepository homeworkRepository;
    private final StudentHomeworkRepository studentHomeworkRepository;
    private final UserSession userSession;

    @Transactional(readOnly = true)
    public DataDto<List<StudentResponse>> getAll() {
        Object data = cacheManagerService.get(studentMapper.hashCode() + "", CachePrefix.STUDENT);
        if (data != null) {
            return (DataDto<List<StudentResponse>>) data;
        }
        List<StudentResponse> response = studentRepository.findAll().stream().map(
                studentMapper::toResponse
        ).toList();
        cacheManagerService.put(studentMapper.hashCode() + "", CachePrefix.STUDENT, new DataDto<>(response));
        return new DataDto<>(response);
    }

    @Transactional(readOnly = true)
    public StudentResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.STUDENT);
        if (data != null) {
            return (StudentResponse) data;
        }
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );
        StudentResponse response = studentMapper.toResponse(student);
        cacheManagerService.put(id.toString(), CachePrefix.STUDENT, response);
        return response;
    }


    @Transactional
    public Long create(StudentRequest request) {
        Student entity = studentMapper.toEntity(request);
        Student save = studentRepository.save(entity);
        cacheManagerService.delete(CachePrefix.STUDENT);
        return save.getId();
    }

    @Transactional
    public Long update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );

        studentMapper.updateFromRequest(request, student);
        Student save = studentRepository.save(student);
        cacheManagerService.delete(CachePrefix.STUDENT);
        return save.getId();
    }

    @Transactional
    public Boolean delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );
        student.markAsDeleted();
        studentRepository.save(student);
        cacheManagerService.delete(CachePrefix.STUDENT);
        return true;
    }

    @Transactional
    public Boolean submitHomework(SubmitHomeworkRequest request) {
        homeworkRepository.findById(request.getHomeworkId()).orElseThrow(
                () -> new GenericNotFoundException("homework.not.found")
        );
        request.setStudentId(userSession.getCurrentUser().getUserId());
        StudentHomework studentHomework = studentMapper.toStudentHomework(request);
        studentHomeworkRepository.save(studentHomework);
        return true;
    }
}
