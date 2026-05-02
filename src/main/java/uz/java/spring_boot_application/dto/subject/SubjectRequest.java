package uz.java.spring_boot_application.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.java.spring_boot_application.entities.Semestr;
import uz.java.spring_boot_application.entities.enums.SubjectType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    private String subjectName;
    private String description;
    private Double kredit;
    private SubjectType type;
    private Integer totalLessonHours;
    private Integer totalResourceCount;
    private Semestr semestr;

}
