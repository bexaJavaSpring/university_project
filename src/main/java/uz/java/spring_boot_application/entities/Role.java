package uz.java.spring_boot_application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role extends Auditable implements GrantedAuthority {

    private String name;

    private String code;

    @Override
    public String getAuthority() {
        return this.code;
    }
}
