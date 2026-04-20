package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.student.StudentFilter;
import uz.java.spring_boot_application.dto.student.StudentRequest;
import uz.java.spring_boot_application.dto.student.StudentResponse;
import uz.java.spring_boot_application.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam Integer page,
            @RequestParam Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer age
    ) {
        return ResponseEntity.ok(studentService.getAll(new StudentFilter(page, limit, sortBy, age)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid StudentRequest request){
        return new ResponseEntity<>(studentService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody StudentRequest request, @PathVariable Long id){
        return  ResponseEntity.ok(studentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok(studentService.delete(id));
    }
}
