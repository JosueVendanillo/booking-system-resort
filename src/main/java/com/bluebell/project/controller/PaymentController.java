package com.bluebell.project.controller;

import com.bluebell.project.dto.PaymentCreateRequest;
import com.bluebell.project.dto.PaymentDto;
import com.bluebell.project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
//@CrossOrigin(origins = "http://localhost:4173") // React dev server
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ✅ Create new payment (using bookingCode)
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentCreateRequest request) {
        PaymentDto payment = paymentService.createPayment(request);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/payment-home-user")
    public ResponseEntity<PaymentDto> createPaymentFromHomePage(
        @RequestBody PaymentCreateRequest request
    ){
        PaymentDto payment = paymentService.createPaymentFromHomePage(request);
        return ResponseEntity.ok(payment);
    }

    // ✅ Get all payments
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // ✅ Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // ✅ Get payments by booking code
    @GetMapping("/booking/{bookingCode}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByBookingCode(@PathVariable String bookingCode) {
        return ResponseEntity.ok(paymentService.getPaymentsByBookingCode(bookingCode));
    }


    @PostMapping("/complete/{bookingCode}")
    public ResponseEntity<PaymentDto> completePayment(
            @PathVariable String bookingCode,
            @RequestParam String paymentMethod
    ) {
        PaymentDto completedPayment = paymentService.completeRemainingPayment(bookingCode, paymentMethod);
        return ResponseEntity.ok(completedPayment);
    }



}
