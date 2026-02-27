package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.dto.faculty.FacultyRequest;
import uz.java.spring_boot_application.dto.faculty.FacultyResponse;
import uz.java.spring_boot_application.dto.university.UniversityResponse;
import uz.java.spring_boot_application.entities.Faculty;
import uz.java.spring_boot_application.entities.University;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.UniversityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final UniversityRepository universityRepository;

    public List<FacultyResponse> getAll() {
        List<Faculty> list = facultyRepository.findAllCustom();
        List<FacultyResponse> response = new ArrayList<>();
        // for i
//        for (int i = 0; i < list.size(); i++) {
//
//        }
        // for each bilan
        for (Faculty faculty : list) {
            FacultyResponse facultyResponse = new FacultyResponse();
            facultyResponse.setId(facultyResponse.getId());
            facultyResponse.setName(faculty.getName());
            facultyResponse.setAddress(faculty.getAddress());
//            facultyResponse.setUniversityId(faculty.getUniversity().getId());
            University university = faculty.getUniversity();
            // builder usuli
            UniversityResponse build = UniversityResponse.builder()
                    .id(university.getId())
                    .name(university.getName())
                    .phone(university.getPhone())
                    .email(university.getEmail())
                    .logo(university.getLogo())
                    .build();
            // oddiy obyekt olib set qilib chiqish
//            UniversityResponse response1 = new UniversityResponse();
//            response1.setId(response1.getId());
//            response1.setName(response1.getName());
//            response1.setPhone(response1.getPhone());

            facultyResponse.setUniversity(build);
            response.add(facultyResponse);
        }
        return response;
    }

    public FacultyResponse getOne(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Faculty not found")
        );
        University university = faculty.getUniversity();
        FacultyResponse build = FacultyResponse.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .address(faculty.getAddress())
                .university(UniversityResponse.builder()
                        .id(university.getId())
                        .name(university.getName())
                        .phone(university.getPhone())
                        .email(university.getEmail())
                        .address(university.getAddress())
                        .build())
                .build();
        return build;
    }

    public Long create(FacultyRequest request) {
        var faculty = new Faculty();
        faculty.setName(request.getName());
        faculty.setAddress(request.getAddress());
        University university = universityRepository.findById(request.getUniversityId()).orElseThrow(
                () -> new RuntimeException("University not found")
        );
        faculty.setUniversity(university);
        facultyRepository.save(faculty);
        return faculty.getId();
    }

    public Long update(Long id, FacultyRequest request) {
        var faculty = facultyRepository.getReferenceById(id);
        if (faculty == null)
            throw new RuntimeException("Faculty not found");
        if (request.getName() != null)
            faculty.setName(request.getName());
        if (request.getAddress() != null)
            faculty.setAddress(request.getAddress());
        if (request.getUniversityId() != null) {
            University university = universityRepository.findById(request.getUniversityId()).orElseThrow(
                    () -> new RuntimeException("University not found")
            );
            faculty.setUniversity(university);
        }
        facultyRepository.save(faculty);
        return id;
    }

    public Boolean delete(Long id) {
        var faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null)
            throw new RuntimeException("Faculty not found");
        // CTRL + alt + L bossa kodni style ini taxlab beradi Intellij
        // CTRL + alt + O keraksiz import va code lani tozalaydi
        faculty.markAsDeleted();
        facultyRepository.save(faculty);
        return true;
    }
}
