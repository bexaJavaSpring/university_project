package uz.java.spring_boot_application.dto.university;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String logo;
    private List<String> attachmentUrls;
}
