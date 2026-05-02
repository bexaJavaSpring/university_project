package uz.java.spring_boot_application.dto.lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.java.spring_boot_application.entities.enums.LessonType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LessonResponse
{
    String title;
    LessonType type;
    List<String> attachmentUrls = new ArrayList<>();
}
