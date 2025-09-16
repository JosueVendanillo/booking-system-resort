package com.bluebell.project.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4173")
public class DashboardController {

    @GetMapping
    public Map<String, Object> getDashboardStats() {
        // Later fetch these from DB/services
        return Map.of(
                "grossRevenue", 3432443,
                "totalBookings", 4324,
                "totalAccounts", 2344,
                "totalCustomers", 43243,
                "totalAmenities", 443243,
                "availableRooms", 432432,
                "billCount", 5454
        );
    }
}
