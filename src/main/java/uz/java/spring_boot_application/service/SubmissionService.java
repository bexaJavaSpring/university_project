package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.submission.SubmissionRequestDto;
import uz.java.spring_boot_application.dto.submission.SubmissionResponseDto;
import uz.java.spring_boot_application.entities.*;
import uz.java.spring_boot_application.exception.CustomAccessDeniedException;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.SubmissionMapper;
import uz.java.spring_boot_application.repository.HomeworkRepository;
import uz.java.spring_boot_application.repository.StudentRepository;
import uz.java.spring_boot_application.repository.SubmissionRepository;
import uz.java.spring_boot_application.repository.TeacherRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final SubmissionMapper submissionMapper;
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    public Long create(SubmissionRequestDto dto) {
        Homework homework = homeworkRepository.findById(dto.getHomeworkId())
                .orElseThrow(() -> new GenericNotFoundException("Homework not found"));

        CustomUserDetails userDetails = UserSession.getCurrentUser();
        User user = userDetails.getUser();
        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();
        if (roles.contains("ROLE_STUDENT")) {
            Student student = studentRepository.findByUsername(user.getUsername());
            boolean hasAccess = homework.getGroup().getId().equals(student.getGroup().getId());
            if (!hasAccess) {
                throw new CustomAccessDeniedException("Access denied");
            }
        }
        Submission submission = submissionMapper.toEntity(dto);
        return submissionRepository.save(submission).getId();
    }

    public Long update(Long id, SubmissionRequestDto dto) {
        Submission submission = submissionRepository.findById(id).orElseThrow(() ->
                new GenericNotFoundException("Submission not found"));
        Student student = studentRepository.
                findById(id).orElseThrow(() -> new GenericNotFoundException("Student not found"));
        Homework homework = homeworkRepository.findById(dto.getHomeworkId())
                .orElseThrow(() -> new GenericNotFoundException("Homework not found"));
        CustomUserDetails userDetails = UserSession.getCurrentUser();
        User user = userDetails.getUser();
        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();
        if (roles.contains("ROLE_STUDENT")) {
            boolean hasAccess = homework.getGroup().getId().
                    equals(student.getGroup().getId());
            if (!hasAccess) {
                throw new CustomAccessDeniedException("Access denied");
            }
        }
        submissionMapper.updateSubmissionFromDto(dto, submission);
        return submissionRepository.save(submission).getId();
    }

    public SubmissionResponseDto getOne(Long id) {
        Submission submission = submissionRepository.findById(id).orElseThrow(() ->
                new GenericNotFoundException("Submission not found"));
        CustomUserDetails userDetails = UserSession.getCurrentUser();
        User user = userDetails.getUser();
        List<String> roles = user.getRoles().stream().map(Role::getCode).toList();
        if (roles.contains("ROLE_STUDENT")) {
            boolean hasAccess = submission.getHomework().getGroup().getId().
                    equals(submission.getStudent().getGroup().getId());
            if (!hasAccess) {
                throw new CustomAccessDeniedException("Access denied");
            }
        }
        return submissionMapper.toResponse(submission);
    }

    public boolean delete(Long id) {
        Submission submission = submissionRepository.findById(id).orElseThrow(() ->
                new GenericNotFoundException("Submission not found"));
        submission.markAsDeleted();
        submissionRepository.save(submission);
        return true;
    }
}
