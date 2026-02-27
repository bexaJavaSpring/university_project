package uz.java.spring_boot_application.dto.faculty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacultyRequest {
    @NotNull(message = "name.must.not.be.null")
    @NotBlank(message = "name.must.not.be.blank")
    private String name;
    private String address;
    @NotNull(message = "university.id.must.not.be.null")
    private Long universityId;
}
