package uz.java.spring_boot_application.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "universities")
public class University extends Auditable {

    private String name;

    //    @Size(min = 2, max = 100)
    private String address;

    //    @Pattern(
//            regexp = "^\\+?[0-9]{9,15}$",
//            message = "Telefon raqam noto‘g‘ri formatda"
//    )
    private String phone;

    //    @Email
    private String email;

    private String website;

    private String logo;

//    @Column(columnDefinition = "int DEFAULT 0")
//    private Integer count;

    @ElementCollection
    private List<String> attachmentUrls;


    // @OneToMany Bidirectional usulni 2 tomonlama turi --->>>
    // yani University Faculty ni biladi, Faculty University ni biladi, mappedBy ishlatiladi.
//    @OneToMany(mappedBy = "university")
//    private List<Faculty> facultyList;

    // @OneToMany Bidirectional ni 1 tomonlama usuli --->>
//    @OneToMany
//    @JoinColumn(name = "university_id")
//    private List<Faculty> facultyList;
}
