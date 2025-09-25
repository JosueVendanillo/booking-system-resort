package com.bluebell.project.repository;

import com.bluebell.project.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, String> {

    @Query("SELECT COUNT(*) AS totalAmenities\n" +
            "FROM Amenity")
    Long countTotalAmenities();
}
