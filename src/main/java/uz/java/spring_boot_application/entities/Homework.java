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

//    1. file upload boladi undan objectName oladi Front
//    2. anashu objectName ni Homework create request ga berib yuboradi
//
//    Backend
//    1. shu imageUrls lani Homework entity ga saqlanadi response qaytarish joylarida ham shu field qoshib berib yuboriladi

    private String teacherComment;

    private Double maxBall;
}