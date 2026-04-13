package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uz.java.spring_boot_application.dto.subject.SubjectFilter;
import uz.java.spring_boot_application.dto.subject.SubjectRequest;
import uz.java.spring_boot_application.dto.subject.SubjectResponse;
import uz.java.spring_boot_application.entities.Subjects;
import uz.java.spring_boot_application.mapper.SubjectMapper;
import uz.java.spring_boot_application.repository.SubjectRepository;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.specification.SubjectSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    @Transactional
    public Long create(SubjectRequest subjectRequest) {
        Subjects subject = subjectRepository.findById(subjectRequest.getSubjectId()).orElse(null);
        if (subject == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        return subjectRepository.save(subject).getId();
    }

    public List<SubjectResponse> getAll(SubjectFilter filter) {
        SubjectSpecification spec = new SubjectSpecification(filter);
        List<Subjects> all = subjectRepository.findAll(spec, SearchSpecification.getPageable(filter.getPage(),
                filter.getLimit(), filter.getSortBy())).toList();
        return all.stream().map(subjectMapper::toResponse).toList();
    }
    @Transactional
    public Long update(SubjectRequest subjectRequest, Long subjectId) {
        var subject = subjectRepository.findById(subjectRequest.getSubjectId()).orElseThrow(
                () -> new RuntimeException("Subject not found")
        );

        subjectMapper.updateFromRequest(subjectRequest, subject);
        return subjectRepository.save(subject).getId();
    }
    @Transactional
    public Boolean delete(Long id) {
        Subjects subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found")
        );

        subject.markAsDeleted();
        subjectRepository.save(subject);

        return true;
    }
}
