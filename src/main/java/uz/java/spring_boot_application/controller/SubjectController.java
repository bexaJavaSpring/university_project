package uz.java.spring_boot_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.subject.SubjectFilter;
import uz.java.spring_boot_application.dto.subject.SubjectRequest;
import uz.java.spring_boot_application.dto.subject.SubjectResponse;
import uz.java.spring_boot_application.dto.user.TeacherResponse;
import uz.java.spring_boot_application.service.SubjectService;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getOne(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSubjects(@RequestParam Integer page,
                                            @RequestParam Integer limit,
                                            @RequestParam(required = false) String sortBy,
                                            @RequestParam(required = false) String name
                                            ) {
        return ResponseEntity.ok(subjectService.getAll(new SubjectFilter(page, limit, sortBy, name)));
    }

    @PostMapping()
    public ResponseEntity<?> addSubject(@RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(subjectService.create(subjectRequest));
    }

    @PostMapping("/id")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(subjectService.update(subjectRequest, id));
    }

    @DeleteMapping("/id")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.delete(id));
    }
}
