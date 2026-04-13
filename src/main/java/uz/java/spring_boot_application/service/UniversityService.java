package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.university.UniversityFilter;
import uz.java.spring_boot_application.dto.university.UniversityRequest;
import uz.java.spring_boot_application.dto.university.UniversityResponse;
import uz.java.spring_boot_application.entities.Faculty;
import uz.java.spring_boot_application.entities.University;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.entities.Zamdekan;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.UniversityMapper;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.UniversityRepository;
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.repository.ZamdekanRepository;

import java.util.ArrayList;
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


    public List<UniversityResponse> getAll(UniversityFilter filter) {
        int page = filter.page() != null ? filter.page() : 0;
        int limit = filter.limit() != null ? filter.limit() : 10;
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by(filter.sortBy() != null ? filter.sortBy() : "id").ascending()
        );

        Page<University> allCustom = universityRepository.findAllCustom(filter.name() != null ? filter.name() : "",
                filter.phone() != null ? filter.phone() : "", pageRequest);
        if (allCustom.isEmpty())
            return new ArrayList<>();
        return allCustom.getContent().stream().map(universityMapper::toResponse).toList();
    }

    public UniversityResponse getOne(Long id) {
        University university = universityRepository.findById(id).orElse(null);
        if (university == null)
            throw new GenericNotFoundException("University not found");

        return universityMapper.toResponse(university);
    }

    @Transactional
    public Long create(UniversityRequest request) {
        University university = universityMapper.toEntity(request);
        University response = universityRepository.save(university);
        notificationService.sendNotification("Universitet saqlandi!");
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
