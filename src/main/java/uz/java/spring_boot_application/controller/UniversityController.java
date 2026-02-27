package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.university.UniversityRequest;
import uz.java.spring_boot_application.dto.university.UniversityResponse;
import uz.java.spring_boot_application.service.UniversityService;

import java.util.List;

@RestController
@RequestMapping("/university")
public class UniversityController {

    // Constructor-based DI(Dependency Injection)
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UniversityResponse>> getAll() {
        List<UniversityResponse> all = universityService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(universityService.getOne(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid UniversityRequest request){
        return ResponseEntity.ok(universityService.create(request));
    }

    @PutMapping("/{universityId}")
    public ResponseEntity<Long> update(@PathVariable Long universityId, @RequestBody UniversityRequest request){
        return ResponseEntity.ok(universityService.update(universityId, request));
    }

    @DeleteMapping("/{universityId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long universityId){
        return ResponseEntity.ok(universityService.delete(universityId));
    }
}
