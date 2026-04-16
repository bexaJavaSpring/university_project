package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.group.GroupFilter;
import uz.java.spring_boot_application.dto.group.GroupRequest;
import uz.java.spring_boot_application.dto.group.GroupResponse;
import uz.java.spring_boot_application.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAll(@RequestParam Integer page,
                                                      @RequestParam Integer limit,
                                                      @RequestParam(required = false) String sortBy,
                                                      @RequestParam(required = false)  String name,
                                                      @RequestParam(required = false) String groupNumber,
                                                      @RequestParam(required = false) Long facultyId){
        return ResponseEntity.ok(service.getAll(new GroupFilter(page, limit, sortBy, name, groupNumber, facultyId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return ResponseEntity.ok(service.getOne(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid GroupRequest request){
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody GroupRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok(service.delete(id));
    }
}
