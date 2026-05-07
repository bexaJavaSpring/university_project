package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})  // indexing bu search yani qidiruv operatsiyalarini tezlashtiradi, nagruzkani kamaytiradi. Bu qaysidir table ning qaysidir fieldiga qoyiladi
 // id esa bundan mustasno (chunki Postgresql da id lar agar primary key bo'lsa deafult holatda id automatik index qoshilgan boladi
//Indexing ni hamma field ga qoyish shartmas faqat ko'p ishlatiladigan yani har safar ishlatiladigan joylarga qoyish kk.

//Kamchiliklari: Indexing create, update, delete entity bo'lganda index qayta yangilanadi shuni hisobiga CUD(create, update, delete)
//api lar sekinlashadi. lekin get zaproslar tezlashadi.
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditable {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String keycloakUserId;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING) // bu annotatsiya Data jpa da shu field enum ligini bildiradi
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>();
}
