package uz.java.spring_boot_application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.java.spring_boot_application.entities.enums.Semestr;
import uz.java.spring_boot_application.entities.enums.SubjectType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subjects")
public class Subjects extends Auditable {

    private String name;

    private String description;

    private Double kredit;

    @Enumerated(EnumType.STRING)
    private SubjectType type;

    private Integer totalLessonHours;

    private Integer totalTaskCount;

    private Integer totalResourceCount;

    @Enumerated(EnumType.STRING)
    private Semestr semestr;
}
