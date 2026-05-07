package uz.java.spring_boot_application.dto.homework;

import lombok.Getter;
import lombok.Setter;
import uz.java.spring_boot_application.dto.grade.GradeDto;

import java.util.List;

@Getter
@Setter
public class HomeworkGradeRequestDto {

    private List<GradeDto> gradeList;
    private Long teacherId;
}
