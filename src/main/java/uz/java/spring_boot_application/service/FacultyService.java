package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.config.UserSession;
import uz.java.spring_boot_application.dto.faculty.FacultyFilter;
import uz.java.spring_boot_application.dto.faculty.FacultyRequest;
import uz.java.spring_boot_application.dto.faculty.FacultyResponse;
import uz.java.spring_boot_application.entities.*;
import uz.java.spring_boot_application.exception.CustomAccessDeniedException;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.FacultyMapper;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.UniversityRepository;
import uz.java.spring_boot_application.repository.ZamdekanRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;
import uz.java.spring_boot_application.specification.FacultySpecification;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final UniversityRepository universityRepository;
    @Qualifier("facultyMapper")
    private final FacultyMapper mapper;
    private final UserSession userSession;
    private final ZamdekanRepository zamdekanRepository;
    private final CacheManagerService cacheManagerService;


    public List<FacultyResponse> getAll(FacultyFilter filter) {
        FacultySpecification spec = new FacultySpecification(filter);
        List<Faculty> all = facultyRepository.findAll(spec, SearchSpecification.getPageable(
                filter.getPage(), filter.getLimit(), filter.getSortBy()
        )).toList();
        return all.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FacultyResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.FACULTY);
        if (data != null)
            return (FacultyResponse) data;
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() ->
                new GenericNotFoundException("faculty.not.found")
        );
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        List<String> list = user.getRoles().stream().map(Role::getCode).toList();
        if (list.contains("ROLE_ZAMDEKAN")) {
            Zamdekan zamdekan = zamdekanRepository.findByUsername(user.getUsername());
            if (!id.equals(zamdekan.getFaculty().getId())) {
                throw new CustomAccessDeniedException("access.denied");
            }
        }
        FacultyResponse response = mapper.toResponse(faculty);
        cacheManagerService.put(id.toString(), CachePrefix.FACULTY, response);
        return response;
    }
    @Transactional
    public Long create(FacultyRequest request) {
        universityRepository.findById(request.getUniversityId()).orElseThrow(
                () -> new GenericNotFoundException("university.not.found")
        );
        Faculty faculty = mapper.toEntity(request);
        facultyRepository.save(faculty);
        return faculty.getId();
    }
    @Transactional
    public Long update(Long id, FacultyRequest request) {
        var faculty = facultyRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("faculty.not.found")
        );
        mapper.updateFromRequest(request, faculty);
        if (request.getUniversityId() != null) {
            University university = universityRepository.findById(request.getUniversityId()).orElseThrow(
                    () -> new GenericNotFoundException("university.not.found")
            );
            faculty.setUniversity(university);
        }
        facultyRepository.save(faculty);
//        cacheManagerService.delete(CachePrefix.FACULTY);
        return id;
    }
    @Transactional
    public Boolean delete(Long id) {
        var faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null)
            throw new GenericNotFoundException("faculty.not.found");
        // CTRL + alt + L bossa kodni style ini taxlab beradi Intellij
        // CTRL + alt + O keraksiz import va code lani tozalaydi
        faculty.markAsDeleted();
        facultyRepository.save(faculty);
        return true;
    }
}
