package uz.java.spring_boot_application.dto.university;

import lombok.*;

import java.io.Serializable;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityResponse implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String logo;
    private List<String> attachmentUrls;
}
