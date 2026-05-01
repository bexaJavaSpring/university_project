package uz.java.spring_boot_application.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import uz.java.spring_boot_application.dto.grade.GradeDto;

import java.util.List;

@Entity
@Table(name = "homework_grade_sheets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeworkGradeSheet extends Auditable {

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<GradeDto> gradeList;

    @OneToOne
    @JoinColumn(name = "homework_id")
    private Homework homework;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


}
