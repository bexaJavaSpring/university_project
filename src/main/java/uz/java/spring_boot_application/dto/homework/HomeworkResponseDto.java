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
public class HomeworkResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Long fileId;
}
