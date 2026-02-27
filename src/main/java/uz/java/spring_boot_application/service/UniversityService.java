package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.dto.UniversityRequest;
import uz.java.spring_boot_application.dto.UniversityResponse;
import uz.java.spring_boot_application.entities.University;
import uz.java.spring_boot_application.repository.UniversityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;


    public List<UniversityResponse> getAll() {
        List<University> all = universityRepository.findAll();
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
}
