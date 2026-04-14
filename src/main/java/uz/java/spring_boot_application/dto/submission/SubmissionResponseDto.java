package uz.java.spring_boot_application.dto.submission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionResponseDto {
    private Long id;
    private String homeworkTitle;
    private Long fileId;
    private String textAnswer;

}
