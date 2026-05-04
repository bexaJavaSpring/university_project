package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.lesson.LessonFilter;
import uz.java.spring_boot_application.dto.lesson.LessonRequest;
import uz.java.spring_boot_application.dto.lesson.LessonResponse;
import uz.java.spring_boot_application.entities.enums.LessonType;
import uz.java.spring_boot_application.service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService service;

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LessonResponse>> getAll(@RequestParam Integer page,
                                                       @RequestParam Integer limit,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(required = false) String title,
                                                       @RequestParam(required = false) LessonType type){
        return ResponseEntity.ok(service.getAll(new LessonFilter(page, limit, sortBy, title, type)));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid LessonRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody LessonRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        return ResponseEntity.ok(service.delete(id));
    }

}
