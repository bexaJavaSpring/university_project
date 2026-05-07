package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.subject.SubjectFilter;
import uz.java.spring_boot_application.dto.subject.SubjectRequest;
import uz.java.spring_boot_application.dto.subject.SubjectResponse;
import uz.java.spring_boot_application.entities.Subjects;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.SubjectMapper;
import uz.java.spring_boot_application.repository.SubjectRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.specification.SubjectSpecification;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final CacheManagerService cacheManagerService;
    private final UserSession userSession;

    @Transactional
    public Long create(SubjectRequest subjectRequest) {
        Subjects subject = subjectMapper.toEntity(subjectRequest);
        subjectRepository.save(subject);
        cacheManagerService.delete(CachePrefix.SUBJECT);
        return subject.getId();
    }

    @Transactional(readOnly = true)
    public SubjectResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.SUBJECT);
        if (data != null) {
            return (SubjectResponse) data;
        }
        Subjects subject = subjectRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("subjet.not.found")
        );
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        SubjectResponse response = subjectMapper.toResponse(subject);
        cacheManagerService.put(id.toString(), CachePrefix.SUBJECT, response);
        return response;

    }

    @Transactional(readOnly = true)
    public DataDto<List<SubjectResponse>> getAll(SubjectFilter filter) {
        Object data = cacheManagerService.get(filter.hashCode() + "", CachePrefix.SUBJECT);
        if (data != null) {
            return (DataDto<List<SubjectResponse>>) data;
        }
        SubjectSpecification spec = new SubjectSpecification(filter);
        List<Subjects> all = subjectRepository.findAll(spec, SearchSpecification.getPageable(filter.getPage(),
                filter.getLimit(), filter.getSortBy())).toList();
        List<SubjectResponse> response = all.stream().map(subjectMapper::toResponse).toList();
        cacheManagerService.put(filter.hashCode() + "", CachePrefix.SUBJECT, new DataDto<>(response));
        return new DataDto<>(response);
    }

    @Transactional
    public Long update(SubjectRequest subjectRequest, Long subjectId) {
        var subject = subjectRepository.findById(subjectId).
                orElseThrow(() -> new RuntimeException("Subject not found")
        );

        subjectMapper.updateFromRequest(subjectRequest, subject);
        subjectRepository.save(subject);
        cacheManagerService.delete(CachePrefix.SUBJECT);
        return subject.getId();
    }

    @Transactional
    public Boolean delete(Long id) {
        Subjects subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found")
        );

        subject.markAsDeleted();
        subjectRepository.save(subject);
        cacheManagerService.delete(CachePrefix.SUBJECT);
        return true;
    }
}
