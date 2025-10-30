package com.bluebell.project.repository;

import com.bluebell.project.model.RoomInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("SELECT r.totalRooms FROM RoomInventory r WHERE r.roomType = :roomType")
    Integer findTotalRoomsByType(@Param("roomType") String roomType);

    @Query("SELECT r.availableRooms FROM RoomInventory r WHERE r.roomType = :roomType")
    Integer findAvailableRoomsByType(@Param("roomType") String roomType);

    @Query("SELECT r.roomMaxCapacity FROM RoomInventory r WHERE r.roomType = :roomType")
    Integer checkRoomCapacity(@Param("roomType") String roomType);

    @Query(value = "SELECT room_type, room_max_capacity FROM room_inventory", nativeQuery = true)
    List<Object[]> findAllRoomTypesAndCapacities();
}
