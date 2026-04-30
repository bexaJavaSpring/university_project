package uz.java.spring_boot_application.entities;


import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Setter;
import org.hibernate.annotations.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uz.java.spring_boot_application.dto.file.AttachmentDto;

import java.util.List;


@Entity
@Table(name = "student_homeworks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentHomework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homework_id")
    private Homework homework;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<AttachmentDto> attachmentUrls;

    private String textAnswer;

    private Integer score;

}
