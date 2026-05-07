package uz.java.spring_boot_application.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest {
        private Double salary;
        private String qualification;
        private String experience;
        private Long subjectId;
        private Long facultyId;
        private UserResponse userResponse;

}
