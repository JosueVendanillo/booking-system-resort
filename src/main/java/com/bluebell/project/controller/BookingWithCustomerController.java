package com.bluebell.project.controller;

import com.bluebell.project.dto.BookingWithCustomerRequest;
import com.bluebell.project.model.Booking;
import com.bluebell.project.service.BookingWithCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings-with-customer")
//@CrossOrigin(origins = "*") // React frontend
public class BookingWithCustomerController {

    @Autowired
    private BookingWithCustomerService bookingWithCustomerService;

    @PostMapping
    public ResponseEntity<Booking> createBookingWithCustomer(@RequestBody BookingWithCustomerRequest request) {
        Booking savedBooking = bookingWithCustomerService.createBookingWithCustomer(request);
        return ResponseEntity.ok(savedBooking);
    }


}
