package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.*;
import uz.java.spring_boot_application.entities.enums.GradeType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "grades")
public class Grade extends Auditable {

    @ManyToOne
    private Student student;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Subjects subject;

    private Double score;

    @Enumerated(EnumType.STRING)
    private GradeType type;

    private String comment;
}
