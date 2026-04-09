package uz.java.spring_boot_application.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileEntity extends Auditable {
    @Column(length = 500)
    private String name;

    private Long size;

    private String contentType;

    private String path;

    private String objectName;
}
