package com.bluebell.project.controller;

import com.bluebell.project.dto.DashboardStats;
import com.bluebell.project.service.*;
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

    private final AmenityService amenityService;

    private final PaymentService paymentService;

    private final RoomInventoryService roomInventoryService;

    public DashboardController(DashboardService dashboardService, BookingService bookingService, AmenityService amenityService, PaymentService paymentService, RoomInventoryService roomInventoryService) {
        this.dashboardService = dashboardService;
        this.bookingService = bookingService;
        this.amenityService = amenityService;
        this.paymentService = paymentService;
        this.roomInventoryService = roomInventoryService;
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

    @GetMapping("/total-accounts")
    public ResponseEntity<Map<String, Long>> getTotalAccounts(){
        Long total = dashboardService.getTotalAccounts();
        return  ResponseEntity.ok(Map.of("totalAccounts", total));
    }

    @GetMapping("/total-amenities")
    public ResponseEntity<Map<String, Long>> getTotalAmenities(){
        Long total = amenityService.getTotalAmenities();
        return ResponseEntity.ok(Map.of("totalAmenities", total));
    }

    @GetMapping("/total-bills")
    public ResponseEntity<Map<String,Long>> getTotalBills(){
        Long total = paymentService.getTotalBills();
        return ResponseEntity.ok(Map.of("totalBills",total));
    }
    @GetMapping("/total-rooms")
    public ResponseEntity<Map<String,Long>> getTotalRooms(){
        Long total = roomInventoryService.getTotalRooms();
        return ResponseEntity.ok(Map.of("totalRooms", total));
    }
    @GetMapping("/available-rooms")
    public ResponseEntity<Map<String,Long>> getAvailableRooms(){
        Long total = roomInventoryService.getAvailableRooms();
        return ResponseEntity.ok(Map.of("availableRooms", total));
    }

}
