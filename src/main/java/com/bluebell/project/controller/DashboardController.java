package com.bluebell.project.controller;

import com.bluebell.project.dto.DashboardStats;
import com.bluebell.project.service.BookingService;
import com.bluebell.project.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final BookingService bookingService;

    public DashboardController(DashboardService dashboardService, BookingService bookingService) {
        this.dashboardService = dashboardService;
        this.bookingService = bookingService;
    }

    @GetMapping("/gross-revenue")
    public DashboardStats getDashboard() {
        System.out.println("Controller output fo Gross Revenue: " + dashboardService.getDashboardStats() );
        return dashboardService.getDashboardStats();

    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenue());
    }


    @GetMapping("/total-bookings")
    public ResponseEntity<Map<String, Long>> getTotalBookingsThisYear() {
        Long total = bookingService.getTotalBookingsThisYear();
        return ResponseEntity.ok(Map.of("totalBookings", total));
    }


    @GetMapping("/total-customers")
    public ResponseEntity<Map<String, Long>> getTotalCustomers() {
        Long total = dashboardService.getTotalCustomers();
        return ResponseEntity.ok(Map.of("totalCustomers", total));
    }


}
