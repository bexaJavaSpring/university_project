package uz.java.spring_boot_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.student.StudentHomeworkResponse;
import uz.java.spring_boot_application.dto.user.TeacherFilter;
import uz.java.spring_boot_application.dto.user.TeacherRequest;
import uz.java.spring_boot_application.dto.user.TeacherResponse;
import uz.java.spring_boot_application.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getOne(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam Integer page,
                                    @RequestParam Integer limit,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) Double salary,
                                    @RequestParam(required = false) Long subjectId,
                                    @RequestParam(required = false) Long facultyId) {
        return ResponseEntity.ok(teacherService.getAll(new TeacherFilter(page, limit, sortBy,
                salary, subjectId, facultyId)));
    }

    @PostMapping
    public ResponseEntity<?> addTeacher(@RequestBody TeacherRequest teacherRequest) {
        return ResponseEntity.ok(teacherService.create(teacherRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @RequestBody TeacherRequest teacherRequest) {
        return ResponseEntity.ok(teacherService.update(teacherRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.delete(id));
    }

    @GetMapping("/view-single")
    public ResponseEntity<?> viewStudentHomework(@RequestParam Long studentId, @RequestParam Long homeworkId){
        return ResponseEntity.ok(teacherService.viewStudentHomework(studentId, homeworkId));
    }

    @GetMapping("/all-student-homeworks")
    public ResponseEntity<List<StudentHomeworkResponse>> getAllStudentHomeworks(){
        return ResponseEntity.ok(teacherService.getAllStudentHomeworks());
    }
}
