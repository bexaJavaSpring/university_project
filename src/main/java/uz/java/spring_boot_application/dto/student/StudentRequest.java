package uz.java.spring_boot_application.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    private Integer age;
    private String gender;
    private LocalDate birthDate;
    @NotNull(message = "firstname.must.not.be.null")
    @NotBlank(message = "firstname.must.not.be.blank")
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
