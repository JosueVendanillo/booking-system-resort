package com.bluebell.project.dto;

import java.time.LocalDateTime;

public class PaymentDto {
    private Long id;
    private String bookingCode;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;

    // constructor
    public PaymentDto(Long id, String bookingCode, Double amount, String paymentMethod, LocalDateTime paymentDate) {
        this.id = id;
        this.bookingCode = bookingCode;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    // getters
    public Long getId() { return id; }
    public String getBookingCode() { return bookingCode; }
    public Double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
}
