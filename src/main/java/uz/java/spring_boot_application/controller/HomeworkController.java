package uz.java.spring_boot_application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.service.HomeworkService;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class HomeworkController {
    private final HomeworkService homeworkService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HomeworkRequestDto dto){
        return ResponseEntity.ok(homeworkService.create(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody HomeworkRequestDto dto){
        return ResponseEntity.ok(homeworkService.update(id,dto));
    }
    @GetMapping("/getOne/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return ResponseEntity.ok(homeworkService.getOne(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok(homeworkService.delete(id));
    }

}
