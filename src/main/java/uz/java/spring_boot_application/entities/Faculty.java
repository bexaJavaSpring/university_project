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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    private String phone;

// Lazy - dangasa
// Eager - ishtiyoqli


//    @ManyToOne  // @OneToMany Bidirectioanl usulni 2 tomonlama turini davomi
//    @JoinColumn(name = "university_id")
//    private University university;

    // 1. One-To-Many (birga ko'p)
    // 2. Many-To-One (Kopga bir)
    // 3. One-To-One (Birga bir)
    // 4. Many-To-Many (Ko'pga ko'p)


//    Cascade-Types :
//    ALL,  --> pastdagini hammasini qanoatlantiradi
//    PERSIST, -->> @OneToMany relationship da agar ota entity save bo'lsa child entity automatik save bo'ladi
//    MERGE,  -->> agar @OneToMany relationshipda ota entity update qilinsa child entity ham automatik update bo'ladi
//    REMOVE, -->> agar ota o'chsa unga tegishli child lar ham automatik o'chadi
//    REFRESH, -->> agar ota database dan qayta o'qilsa bolalari ham qayta yuklanib olib kelinadi
//    DETACH; -->> agar ota entity detach qilinsa bolalari ham automatik Persistence Context dan chiqarib yuboriladi(yani connection dan uziladi)
}
