package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homeworks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Homework extends Auditable {

    private String title;

    private String description;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private Long groupId;

    @ElementCollection
    private List<String> attachmentUrls = new ArrayList<>();

    private String teacherComment;

    private Double maxBall;
}