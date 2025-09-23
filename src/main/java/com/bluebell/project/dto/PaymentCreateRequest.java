package com.bluebell.project.dto;

import java.time.LocalDateTime;

public class PaymentCreateRequest {
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;

    // getters / setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
