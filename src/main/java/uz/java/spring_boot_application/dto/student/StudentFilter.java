package uz.java.spring_boot_application.dto.student;

import lombok.*;
import uz.java.spring_boot_application.dto.BaseFilter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentFilter extends BaseFilter {
    private Integer age;

    public StudentFilter(Integer page, Integer limit, String sortBy, Integer age) {
        super(page, limit, sortBy);
        this.age = age;
    }
}
