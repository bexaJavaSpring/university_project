package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.faculty.FacultyFilter;
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
    @PreAuthorize("hasAnyRole('ROLE_REKTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<FacultyResponse>> getAll(@RequestParam Integer page,
                                                        @RequestParam Integer limit,
                                                        @RequestParam(required = false) String sortBy,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Long universityId) {
        return ResponseEntity.ok(facultyService.getAll(new FacultyFilter(page, limit, sortBy, name, universityId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('REKTOR', 'SUPER_ADMIN', 'ZAMDEKAN')")
    public ResponseEntity<FacultyResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getOne(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('REKTOR', 'SUPER_ADMIN')")
    public ResponseEntity<Long> create(@RequestBody @Valid FacultyRequest request) {
        return ResponseEntity.ok(facultyService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_REKTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody FacultyRequest request) {
        return ResponseEntity.ok(facultyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_REKTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.delete(id));
    }
}
