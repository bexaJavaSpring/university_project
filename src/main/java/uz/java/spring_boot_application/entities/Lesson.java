package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.java.spring_boot_application.entities.enums.LessonType;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "lessons")
public class Lesson extends Auditable {

    private String title;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    @Column(name = "file_count")
    private Integer fileCount;

    @ElementCollection
    private List<String> attachmentUrls = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subjects;
}
