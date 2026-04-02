package uz.java.spring_boot_application.dto.faculty;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.java.spring_boot_application.dto.university.UniversityResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacultyResponse {
    Long id;
    String name;
    String address;
    //    Long universityId;
    UniversityResponse university;
}
