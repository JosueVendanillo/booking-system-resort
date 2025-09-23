package com.bluebell.project.controller;

import com.bluebell.project.dto.PaymentCreateRequest;
import com.bluebell.project.dto.PaymentDto;
import com.bluebell.project.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*") // adjust for your frontend
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<PaymentDto> getAllPayments() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        PaymentDto payment = service.getById(id);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/booking/{bookingId}")
    public List<PaymentDto> getPaymentsByBookingId(@PathVariable Long bookingId) {
        return service.getPaymentsByBookingId(bookingId);
    }

    @GetMapping("/booking/code/{bookingCode}")
    public List<PaymentDto> getPaymentsByBookingCode(@PathVariable String bookingCode) {
        return service.getPaymentsByBookingCode(bookingCode);
    }

    @PostMapping("/{bookingId}")
    public ResponseEntity<PaymentDto> createPayment(
            @PathVariable Long bookingId,
            @RequestBody PaymentCreateRequest request
    ) {
        PaymentDto created = service.createPayment(bookingId, request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentCreateRequest request
    ) {
        PaymentDto updated = service.updatePayment(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        service.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
