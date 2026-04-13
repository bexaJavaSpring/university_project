package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.student.StudentRequest;
import uz.java.spring_boot_application.dto.student.StudentResponse;
import uz.java.spring_boot_application.entities.Student;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.StudentMapper;
import uz.java.spring_boot_application.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream().map(
                studentMapper::toResponse
        ).toList();
    }

    public StudentResponse getOne(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );
        return studentMapper.toResponse(student);
    }

    @Transactional
    public Long create(StudentRequest request) {
        Student entity = studentMapper.toEntity(request);
        Student save = studentRepository.save(entity);
        return save.getId();
    }
    @Transactional
    public Long update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );

        studentMapper.updateFromRequest(request, student);
        Student save = studentRepository.save(student);
        return save.getId();
    }
    @Transactional
    public Boolean delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("student.not.found")
        );
        student.markAsDeleted();
        studentRepository.save(student);
        return true;
    }
}
