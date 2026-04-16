package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "homework")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private Attachment file;

    @OneToMany(mappedBy = "homework")
    private List<Submission> submissions;// student yozgan yoki yuborgan javob
}