package uz.java.spring_boot_application.dto.student;

import lombok.Data;
import uz.java.spring_boot_application.dto.file.AttachmentDto;

import java.util.ArrayList;
import java.util.List;
@Data
public class StudentHomeworkResponse {
    private Long studentId;
    private Long homeworkId;
    private List<AttachmentDto> attachmentUrls = new ArrayList<>();
    private String textAnswer;
    private Integer score;
}
