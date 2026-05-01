//package uz.java.spring_boot_application.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import uz.java.spring_boot_application.dto.submission.SubmissionRequestDto;
//import uz.java.spring_boot_application.service.SubmissionService;
//
//@RestController
//@RequestMapping("/api/submission")
//@RequiredArgsConstructor
//public class SubmissionController {
//    private final SubmissionService submissionService;
//
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<?> crate(@RequestBody SubmissionRequestDto dto){
//        return ResponseEntity.ok(submissionService.create(dto));
//    }
//
//    @PutMapping("/update/{id}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SubmissionRequestDto dto){
//        return ResponseEntity.ok(submissionService.update(id,dto));
//    }
//
//    @GetMapping("/getOne/{id}")
//    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
//    public ResponseEntity<?> getOne(@PathVariable Long id){
//        return ResponseEntity.ok(submissionService.getOne(id));
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id){
//        return ResponseEntity.ok(submissionService.delete(id));
//    }
//
//
//
//}
