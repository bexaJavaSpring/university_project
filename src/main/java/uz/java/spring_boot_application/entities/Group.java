package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "groups")
public class Group extends Auditable {

    private String name;

    private String groupNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL) //mappedBy ga Student dagi "group" field nomini ko'rsatamiz
    private List<Student> students = new ArrayList<>();

//    agar 2ta entity biri birini ichida chaqirilib boglansa bu Bidirectional boglanish bo'ladi
//    bu Java tarafdan bizaga 2taraflama boglanishday tuyuladi lekin bazada 1ta foreign key yaratiladi xolos
}
