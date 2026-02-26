package uz.java.spring_boot_application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.events.Event;
import uz.java.spring_boot_application.dto.UniversityResponse;
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



}
