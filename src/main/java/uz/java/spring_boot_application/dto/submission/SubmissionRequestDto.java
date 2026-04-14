package uz.java.spring_boot_application.dto.submission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionRequestDto {
    private Long homeworkId;
    private Long fileId;
    private String textAnswer;

}
