package uz.java.spring_boot_application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.user.UserFilter;
import uz.java.spring_boot_application.dto.user.UserRequest;
import uz.java.spring_boot_application.dto.user.UserResponse;
import uz.java.spring_boot_application.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<DataDto<List<UserResponse>>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "10")Integer size,
                                                              @RequestParam(defaultValue = "id",required = false) String sortBy,
                                                              @RequestParam(required = false)String firstName,
                                                              @RequestParam(required = false) String lastName) {

        return ResponseEntity.ok(userService.getAll(new UserFilter(page,size,sortBy,firstName,lastName)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getOne(@PathVariable Long id){
        return ResponseEntity.ok(userService.getOne(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid UserRequest request){
        return ResponseEntity.ok(userService.create(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Long> update(@PathVariable Long userId, @RequestBody @Valid UserRequest request){
        return ResponseEntity.ok(userService.update(userId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        return ResponseEntity.ok(userService.delete(id));
    }
}
