package uz.java.spring_boot_application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "groups")
public class Group extends Auditable {

    private String name;

    private String groupNumber;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
}
