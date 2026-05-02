package uz.java.spring_boot_application.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.java.spring_boot_application.dto.BaseFilter;
import uz.java.spring_boot_application.entities.enums.LessonType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonFilter extends BaseFilter {
    private String title;
    private LessonType type;

    public LessonFilter(Integer page, Integer limit, String sortBy, String title, LessonType type) {
        super(page, limit, sortBy);
        this.title = title;
        this.type = type;
    }
}
