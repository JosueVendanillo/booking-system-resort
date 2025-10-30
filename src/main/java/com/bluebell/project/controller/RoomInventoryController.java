package com.bluebell.project.controller;

import com.bluebell.project.model.RoomInventory;
import com.bluebell.project.repository.RoomInventoryRepository;
import com.bluebell.project.service.RoomInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomInventoryController {

    private final RoomInventoryService service;

    private final RoomInventoryRepository repository;

    public RoomInventoryController(RoomInventoryService service, RoomInventoryRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<RoomInventory>> getInventory() {
        return ResponseEntity.ok(service.getAllRooms());
    }

    @PutMapping("/update/{roomType}")
    public ResponseEntity<?> updateAvailability(@PathVariable String roomType, @RequestParam int available) {
        service.updateAvailability(roomType, available);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomType}/total")
    public ResponseEntity<Integer> getTotalRoomsByType(@PathVariable String roomType) {
        return ResponseEntity.ok(service.getTotalRooms(roomType));
    }


    @GetMapping("/room-capacities")
    public List<Object[]> getAllRoomCapacities() {
        return repository.findAllRoomTypesAndCapacities();
    }

}
