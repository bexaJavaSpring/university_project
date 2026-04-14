package uz.java.spring_boot_application.dto.homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkRequestDto {

    private String title;
    private String description;
    private LocalDateTime deadline;
    private Long fileId;
    private Long groupId;

}
