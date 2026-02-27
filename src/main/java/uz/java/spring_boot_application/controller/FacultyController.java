package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.faculty.FacultyRequest;
import uz.java.spring_boot_application.dto.faculty.FacultyResponse;
import uz.java.spring_boot_application.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/all")
    public ResponseEntity<List<FacultyResponse>> getAll() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid FacultyRequest request) {
        return ResponseEntity.ok(facultyService.create(request));
    }

    @PutMapping("/{id}")  // ? bu lyuboy tipni qabul qiladi noaniq degani
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody FacultyRequest request) {
        return ResponseEntity.ok(facultyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.delete(id));
    }
}
