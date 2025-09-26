package com.bluebell.project.config;

import com.bluebell.project.model.RoomInventory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "room")
public class RoomInventoryProperties {

    private List<RoomInventory> inventory;

    public List<RoomInventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<RoomInventory> inventory) {
        this.inventory = inventory;
    }
}
