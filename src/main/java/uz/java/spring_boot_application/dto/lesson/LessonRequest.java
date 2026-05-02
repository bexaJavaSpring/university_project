package uz.java.spring_boot_application.dto.lesson;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.java.spring_boot_application.entities.Subjects;
import uz.java.spring_boot_application.entities.enums.LessonType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonRequest {
    @NotNull(message = "title.must.not.be.null")
    String title;
    LessonType type;
    List<String> attachmentUrls = new ArrayList<>();
    @NotNull(message = "subject.must.not.be.null")
    Subjects subjects;
}
