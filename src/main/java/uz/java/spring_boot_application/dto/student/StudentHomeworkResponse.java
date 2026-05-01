package uz.java.spring_boot_application.dto.student;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class StudentHomeworkResponse {
    private Long studentId;
    private Long homeworkId;
    private List<String> attachmentUrls = new ArrayList<>();
    private String textAnswer;
    private Integer score;
}
