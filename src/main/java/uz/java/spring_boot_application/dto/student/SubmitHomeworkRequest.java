package uz.java.spring_boot_application.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.java.spring_boot_application.dto.file.AttachmentDto;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitHomeworkRequest {
    @NotNull(message = "homework.id.must.not.be.null")
    private Long homeworkId;
    private List<AttachmentDto> attachmentUrls = new ArrayList<>();
    private String textAnswer;
    @Schema(hidden = true)
    private Long studentId;
}
