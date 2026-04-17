package uz.java.spring_boot_application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.homework.HomeworkFilter;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.service.HomeworkService;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {
    private final HomeworkService homeworkService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> create(@RequestBody HomeworkRequestDto dto){
        return ResponseEntity.ok(homeworkService.create(dto));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody HomeworkRequestDto dto){
        return ResponseEntity.ok(homeworkService.update(id,dto));
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('TEACHER','SUPERADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) Long groupId){
        return ResponseEntity.ok(homeworkService.getAll(new HomeworkFilter(page,size,sortBy,title,groupId)));
    }
    @GetMapping("/getOne/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return ResponseEntity.ok(homeworkService.getOne(id));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok(homeworkService.delete(id));
    }

}
