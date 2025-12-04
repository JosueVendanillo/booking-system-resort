package com.bluebell.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "room_inventory")
public class RoomInventory {

    @Id
    @Column(name = "room_type")
    private String roomType;

    @Column(name="total_rooms")
    private int totalRooms;

    @Column(name = "available_rooms")
    private int availableRooms;

    @Column(name = "room_max_capacity")
    private int roomMaxCapacity;

    @Column(name = "room_rate")
    private double roomRate;

    public RoomInventory() {}

    public RoomInventory(String roomType, int totalRooms, int availableRooms, int roomMaxCapacity) {
        this.roomType = roomType;
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
        this.roomMaxCapacity = roomMaxCapacity;
    }

    // Getters and setters



    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }



    public int getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(int availableRooms) { this.availableRooms = availableRooms; }

    public int getRoomMaxCapacity() {
        return roomMaxCapacity;
    }

    public void setRoomMaxCapacity(int roomMaxCapacity) {
        this.roomMaxCapacity = roomMaxCapacity;
    }


    public double getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
    }
}
