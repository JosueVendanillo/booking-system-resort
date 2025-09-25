package com.bluebell.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_inventory")
public class RoomInventory {

    @Id
    private String roomType;

    private int totalRooms;
    private int availableRooms;

    public RoomInventory() {}

    public RoomInventory(String roomType, int totalRooms, int availableRooms) {
        this.roomType = roomType;
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
    }

    // Getters and setters
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

    public int getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(int availableRooms) { this.availableRooms = availableRooms; }
}
