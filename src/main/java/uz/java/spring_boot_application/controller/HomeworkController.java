package uz.java.spring_boot_application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.homework.HomeworkCreateRequestDto;
import uz.java.spring_boot_application.dto.homework.HomeworkFilter;
import uz.java.spring_boot_application.dto.homework.HomeworkRequestDto;
import uz.java.spring_boot_application.service.HomeworkService;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
public class HomeworkController {
    private final HomeworkService homeworkService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> create(@RequestBody HomeworkCreateRequestDto dto){
        return ResponseEntity.ok(homeworkService.create(dto));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody HomeworkRequestDto dto){
        return ResponseEntity.ok(homeworkService.update(id,dto));
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) Long groupId){
        return ResponseEntity.ok(homeworkService.getAll(new HomeworkFilter(page,size,sortBy,title,groupId)));
    }
    @GetMapping("/getOne/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN','STUDENT')")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return ResponseEntity.ok(homeworkService.getOne(id));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok(homeworkService.delete(id));
    }


}
