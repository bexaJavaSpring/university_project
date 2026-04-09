package uz.java.spring_boot_application.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attachment_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] bytes; // faylning o‘zi

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;
}
