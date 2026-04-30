//package uz.java.spring_boot_application.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import uz.java.spring_boot_application.config.UserSession;
//import uz.java.spring_boot_application.dto.submission.SubmissionRequestDto;
//import uz.java.spring_boot_application.dto.submission.SubmissionResponseDto;
//import uz.java.spring_boot_application.entities.*;
//import uz.java.spring_boot_application.exception.CustomAccessDeniedException;
//import uz.java.spring_boot_application.exception.GenericNotFoundException;
//import uz.java.spring_boot_application.mapper.SubmissionMapper;
//import uz.java.spring_boot_application.repository.HomeworkRepository;
//import uz.java.spring_boot_application.repository.StudentRepository;
//import uz.java.spring_boot_application.repository.TeacherRepository;
//import uz.java.spring_boot_application.security.CustomUserDetails;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SubmissionService {
//    private final SubmissionRepository submissionRepository;
//    private final SubmissionMapper submissionMapper;
//    private final HomeworkRepository homeworkRepository;
//    private final StudentRepository studentRepository;
//    private final TeacherRepository teacherRepository;
//    private final UserSession userSession;
//
//
//    public Long create(SubmissionRequestDto dto) {
//        Homework homework = homeworkRepository.findById(dto.getHomeworkId())
//                .orElseThrow(() -> new GenericNotFoundException("Homework not found"));
//
//        CustomUserDetails userDetails = userSession.getCurrentUser();
//        User user = userDetails.getUser();
//        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();
//        if (roles.contains("ROLE_STUDENT")) {
//            Student student = studentRepository.findByUsername(user.getUsername());
//            boolean hasAccess = homework.getGroup().getId().equals(student.getGroup().getId());
//            if (!hasAccess) {
//                throw new CustomAccessDeniedException("Access denied");
//            }
//        }
//        StudentHomework studentHomework = submissionMapper.toEntity(dto);
//        return submissionRepository.save(studentHomework).getId();
//    }
//
//    public Long update(Long id, SubmissionRequestDto dto) {
//        StudentHomework studentHomework = submissionRepository.findById(id).orElseThrow(() ->
//                new GenericNotFoundException("StudentHomework not found"));
//        Student student = studentRepository.
//                findById(id).orElseThrow(() -> new GenericNotFoundException("Student not found"));
//        Homework homework = homeworkRepository.findById(dto.getHomeworkId())
//                .orElseThrow(() -> new GenericNotFoundException("Homework not found"));
//        CustomUserDetails userDetails = userSession.getCurrentUser();
//        User user = userDetails.getUser();
//        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();
//        if (roles.contains("ROLE_STUDENT")) {
//            boolean hasAccess = homework.getGroup().getId().
//                    equals(student.getGroup().getId());
//            if (!hasAccess) {
//                throw new CustomAccessDeniedException("Access denied");
//            }
//        }
//        submissionMapper.updateSubmissionFromDto(dto, studentHomework);
//        return submissionRepository.save(studentHomework).getId();
//    }
//
//    public SubmissionResponseDto getOne(Long id) {
//        StudentHomework studentHomework = submissionRepository.findById(id).orElseThrow(() ->
//                new GenericNotFoundException("StudentHomework not found"));
//        CustomUserDetails userDetails = userSession.getCurrentUser();
//        User user = userDetails.getUser();
//        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();
//        if (roles.contains("ROLE_STUDENT")) {
//            boolean hasAccess = studentHomework.getHomework().getGroup().getId().
//                    equals(studentHomework.getStudent().getGroup().getId());
//            if (!hasAccess) {
//                throw new CustomAccessDeniedException("Access denied");
//            }
//        }
//        return submissionMapper.toResponse(studentHomework);
//    }
//
//    public boolean delete(Long id) {
//        StudentHomework studentHomework = submissionRepository.findById(id).orElseThrow(() ->
//                new GenericNotFoundException("StudentHomework not found"));
//        studentHomework.markAsDeleted();
//        submissionRepository.save(studentHomework);
//        return true;
//    }
//}
