package uz.java.spring_boot_application.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.java.spring_boot_application.entities.Gender;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String email;
    String firstName;
    String lastName;
    LocalDate birthDate;
    Gender gender;
    String username;
    String password;
}
