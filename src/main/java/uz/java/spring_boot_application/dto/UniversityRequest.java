package uz.java.spring_boot_application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversityRequest {
    @NotNull(message = "name.must.not.be.null")
    @NotBlank(message = "name.must.not.be.blank")
    private String name;
    private String address;
    private String phone;
    private String email;
    private String logo;
    private List<String> attachmentUrls;
}
