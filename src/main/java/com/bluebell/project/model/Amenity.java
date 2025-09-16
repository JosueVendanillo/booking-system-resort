package com.bluebell.project.model;

import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

@Table(name = "amenities")
public class Amenity {
    @Id
    private String id;
    private String name;
    private String description;

    public Amenity() {}

    public Amenity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
