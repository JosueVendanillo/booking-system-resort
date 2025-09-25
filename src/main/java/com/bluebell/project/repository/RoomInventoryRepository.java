package com.bluebell.project.repository;

import com.bluebell.project.model.RoomInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomInventoryRepository extends JpaRepository<RoomInventory, String> {
    Optional<RoomInventory> findByRoomType(String roomType);


    @Query("SELECT \n" +
            "    SUM(totalRooms) AS totalRooms\n" +
            "FROM RoomInventory")
    Long countTotalRooms();

    @Query("SELECT \n" +
            "    SUM(availableRooms) AS availableRooms\n" +
            "FROM RoomInventory")
    Long countAvailableRooms();
}
