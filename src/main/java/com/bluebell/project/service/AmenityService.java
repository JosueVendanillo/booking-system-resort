package com.bluebell.project.service;

import com.bluebell.project.model.Amenity;
import com.bluebell.project.repository.AmenityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {
    private final AmenityRepository repository;

    public AmenityService(AmenityRepository repository) {
        this.repository = repository;
    }

    public List<Amenity> getAll() {
        return repository.findAll();
    }

    public Optional<Amenity> getById(String id) {
        return repository.findById(id);
    }

    public Amenity create(Amenity amenity) {
        return repository.save(amenity);
    }

    public Amenity update(String id, Amenity updatedAmenity) {
        return repository.findById(id).map(existing -> {
            existing.setName(updatedAmenity.getName());
            existing.setDescription(updatedAmenity.getDescription());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Amenity not found"));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }


    public Long getTotalAmenities() {
        return repository.countTotalAmenities();
    }
}
