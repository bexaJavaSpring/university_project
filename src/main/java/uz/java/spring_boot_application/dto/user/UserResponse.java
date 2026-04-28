package uz.java.spring_boot_application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.java.spring_boot_application.entities.Role;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate birthDate;
    private Set<Role> roles;

}
