package uz.java.spring_boot_application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.java.spring_boot_application.dto.room.RoomFilter;
import uz.java.spring_boot_application.dto.room.RoomRequest;
import uz.java.spring_boot_application.entities.Room;
import uz.java.spring_boot_application.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam Integer page,
                                    @RequestParam Integer limit,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String roomNumber,
                                    @RequestParam(required = false) Long groupId) {
        return ResponseEntity.ok(roomService.getAll(new RoomFilter(page, limit, sortBy, name, roomNumber, groupId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.create(roomRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody RoomRequest roomRequest, @PathVariable Long id) {
        return new ResponseEntity<>(roomService.update(roomRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return new ResponseEntity<>(roomService.delete(id), HttpStatus.OK);
    }
}
