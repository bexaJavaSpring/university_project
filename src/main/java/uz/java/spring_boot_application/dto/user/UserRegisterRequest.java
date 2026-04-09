package uz.java.spring_boot_application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
