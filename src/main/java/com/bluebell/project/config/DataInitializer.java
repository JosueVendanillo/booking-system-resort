package com.bluebell.project.config;

import com.bluebell.project.model.RoomInventory;
import com.bluebell.project.repository.RoomInventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final RoomInventoryProperties properties;

    public DataInitializer(
            RoomInventoryProperties properties) {
        this.properties = properties;
    }

    @Bean
    CommandLineRunner initDatabase(RoomInventoryRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                for (RoomInventory room : properties.getInventory()) {
                    repo.save(room);
                }
                System.out.println(" Room inventory initialized from configuration.");
            } else {
                System.out.println("ℹ Room inventory already initialized, skipping.");
            }
        };
    }
}
