package com.bluebell.project.controller;

import com.bluebell.project.model.Amenity;
import com.bluebell.project.service.AmenityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
//@CrossOrigin(origins = "*")
public class AmenityController {
    private final AmenityService service;

    public AmenityController(AmenityService service) {
        this.service = service;
    }

    @GetMapping
    public List<Amenity> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Amenity> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Amenity create(@RequestBody Amenity amenity) {
        return service.create(amenity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Amenity> update(@PathVariable String id, @RequestBody Amenity amenity) {
        try {
            return ResponseEntity.ok(service.update(id, amenity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
