package com.bluebell.project.dto;

import java.time.LocalDateTime;

public class PaymentDto {
    private Long id;
    private String bookingCode;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;

    private String status;   // ✅ added status field

    private Double remainingBalance;

    private String referenceNumber;

    // constructor
    public PaymentDto(Long id, String bookingCode, Double amount, String paymentMethod, LocalDateTime paymentDate, String status, Double remainingBalance,String referenceNumber) {
        this.id = id;
        this.bookingCode = bookingCode;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.status = status;
        this.remainingBalance = remainingBalance;
        this.referenceNumber = referenceNumber;
    }

    // getters


    public Long getId() {
        return id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public Double getRemainingBalance() {
        return remainingBalance;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
