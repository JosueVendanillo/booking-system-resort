package com.bluebell.project.service;

import com.bluebell.project.model.RoomInventory;
import com.bluebell.project.repository.RoomInventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomInventoryService {

    private final RoomInventoryRepository repository;


    public RoomInventoryService(RoomInventoryRepository repository) {
        this.repository = repository;
    }

    public List<RoomInventory> getAllRooms() {
        return repository.findAll();
    }

    public RoomInventory getRoomByType(String roomType) {
        return repository.findById(roomType).orElse(null);
    }

    public void updateAvailability(String roomType, int available) {
        RoomInventory room = repository.findById(roomType).orElse(null);
        if (room != null) {
            room.setAvailableRooms(available);
            repository.save(room);
        }
    }

    public int getAvailableRooms(String roomType) {
        RoomInventory room = repository.findByRoomType(roomType)
                .orElseThrow(() -> new IllegalArgumentException("Room type not found: " + roomType));
        return room.getAvailableRooms();
    }

    public Integer getTotalRooms(String roomType) {
        return repository.findTotalRoomsByType(roomType);
    }

    @Transactional
    public void decreaseAvailability(String roomType, int amount) {
        RoomInventory room = repository.findByRoomType(roomType)
                .orElseThrow(() -> new IllegalArgumentException("Room type not found"));

        if (room.getAvailableRooms() < amount) {
            throw new IllegalStateException("Not enough available rooms for type: " + roomType);
        }

        room.setAvailableRooms(room.getAvailableRooms() - amount);
        repository.save(room);
    }


    public Long getTotalRooms(){
        return repository.countTotalRooms();
    }

    public Long getAvailableRooms(){
        return  repository.countAvailableRooms();
    }


}
