package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "faculties")
public class Faculty extends Auditable {

    @Column(unique = true, /*nullable = false*/  length = 100, name = "nomi")
    private String name;

    private String address;

    // Unidirectional usul
    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;






//    @ManyToOne  // @OneToMany Bidirectioanl usulni 2 tomonlama turini davomi
//    @JoinColumn(name = "university_id")
//    private University university;

    // 1. One-To-Many (birga ko'p)
    // 2. Many-To-One (Kopga bir)
    // 3. One-To-One (Birga bir)
    // 4. Many-To-Many (Ko'pga ko'p)

}
