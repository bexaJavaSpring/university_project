package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.dto.university.UniversityRequest;
import uz.java.spring_boot_application.dto.university.UniversityResponse;
import uz.java.spring_boot_application.entities.University;
import uz.java.spring_boot_application.repository.UniversityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;


    public List<UniversityResponse> getAll() {
        List<University> all = universityRepository.findAllCustom();
        List<UniversityResponse> response = new ArrayList<>();
        for (University university : all) {
            UniversityResponse universityResponse = new UniversityResponse();
            universityResponse.setId(university.getId());
            universityResponse.setName(university.getName());
            universityResponse.setAddress(university.getAddress());
            universityResponse.setEmail(university.getEmail());
            response.add(universityResponse);
        }
        return response;
    }

    public UniversityResponse getOne(Long id) {
        University university = universityRepository.getReferenceById(id);
        if (Objects.isNull(university))
            throw new RuntimeException("university not found");
        UniversityResponse response = new UniversityResponse();
        response.setId(university.getId());
        response.setName(university.getName());
        response.setAddress(university.getAddress());
        response.setEmail(university.getEmail());
        return response;
    }

    public Long create(UniversityRequest request) {
        University university = new University();
        university.setName(request.getName());
        university.setAddress(request.getAddress());
        university.setEmail(request.getEmail());
        university.setPhone(request.getPhone());
        university.setLogo(request.getLogo());
        university.setAttachmentUrls(request.getAttachmentUrls());
        University response = universityRepository.save(university);// Alt + Enter bossa o`zgaruvchiga oladi
        return response.getId();
    }

    public Long update(Long universityId, UniversityRequest request) {
        Optional<University> optional = universityRepository.findById(universityId);
        if (!optional.isPresent())
            throw new RuntimeException("University not found");
        University university = optional.get();
        if (request.getName() != null)
            university.setName(request.getName());
        if (request.getAddress() != null)
            university.setAddress(request.getAddress());
        if (request.getEmail() != null)
            university.setEmail(request.getEmail());
        if (request.getPhone() != null)
            university.setPhone(request.getPhone());
        if (request.getAttachmentUrls() != null)
            university.setAttachmentUrls(request.getAttachmentUrls());
        universityRepository.save(university);
        return universityId;
    }

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
}
