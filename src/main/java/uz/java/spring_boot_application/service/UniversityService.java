package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.notification.NotificationRequest;
import uz.java.spring_boot_application.dto.university.UniversityFilter;
import uz.java.spring_boot_application.dto.university.UniversityRequest;
import uz.java.spring_boot_application.dto.university.UniversityResponse;
import uz.java.spring_boot_application.entities.*;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.UniversityMapper;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.UniversityRepository;
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.repository.ZamdekanRepository;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.specification.UniversitySpecification;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;
    private final ZamdekanRepository zamdekanRepository;
    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final CacheManagerService cacheManagerService;

    @Transactional(readOnly = true)
    public DataDto<List<UniversityResponse>> getAll(UniversityFilter filter) {
        Object data = cacheManagerService.get(filter.hashCode() + "", CachePrefix.UNIVERSITY);
        if (data != null) {
            return (DataDto<List<UniversityResponse>>) data;
        }
        List<UniversityResponse> response = universityRepository.findAll(new UniversitySpecification(filter),
                SearchSpecification.getPageable(filter.page(), filter.limit())).map(universityMapper::toResponse).toList();
        cacheManagerService.put(filter.hashCode() + "", CachePrefix.UNIVERSITY, new DataDto<>(response));
        return new DataDto<>(response);
    }

    @Transactional(readOnly = true)
    public UniversityResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.UNIVERSITY);
        if (data != null)
            return (UniversityResponse) data;
        University university = universityRepository.findById(id).orElse(null);
        if (university == null)
            throw new GenericNotFoundException("University not found");

        UniversityResponse response = universityMapper.toResponse(university);
        cacheManagerService.put(id.toString(), CachePrefix.UNIVERSITY, response);
        return response;
    }

    @Transactional
    public Long create(UniversityRequest request) {
        University university = universityMapper.toEntity(request);
        University response = universityRepository.save(university);
        cacheManagerService.delete(CachePrefix.UNIVERSITY);
        return response.getId();
    }

    @Transactional
    public Long update(Long universityId, UniversityRequest request) {
        Optional<University> optional = universityRepository.findById(universityId);
        if (!optional.isPresent())
            throw new RuntimeException("University not found");
        University university = optional.get();
        universityMapper.updateFromRequest(request, university);
        universityRepository.save(university);
        cacheManagerService.delete(CachePrefix.UNIVERSITY);
        return universityId;
    }

    @Transactional
    public Boolean delete(Long universityId) {
        // 1-usul
//        University university = universityRepository.findById(universityId).orElse(null);
        // 2-usul
//        University university = universityRepository.getReferenceById(universityId);
//        if (university == null)
//            throw new RuntimeException("university not found");
        // 3-usul
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("university not found"));
        // soft deleted boldi
        university.markAsDeleted();
        universityRepository.save(university);
        cacheManagerService.delete(CachePrefix.UNIVERSITY);
        // hard deleted
//        universityRepository.delete(university);
        return true;
    }

    @Transactional
    public Boolean attachZamdekan(Long facultyId, List<Long> zamdekanIds) {
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(
                () -> new GenericNotFoundException("Faculty not found")
        );
        zamdekanIds.stream().forEach(zamdekanId -> {
            User user = userRepository.findById(zamdekanId).orElseThrow(
                    () -> new GenericNotFoundException("User not found")
            );
            Zamdekan zamdekan = zamdekanRepository.findById(user.getId()).orElseThrow(
                    () -> new GenericNotFoundException("Zamdekan not found")
            );
            zamdekan.setFaculty(faculty);
            zamdekanRepository.save(zamdekan);
        });

        return true;
    }
}
