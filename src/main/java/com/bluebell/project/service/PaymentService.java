package com.bluebell.project.service;

import com.bluebell.project.dto.PaymentCreateRequest;
import com.bluebell.project.dto.PaymentDto;
import com.bluebell.project.model.Booking;
import com.bluebell.project.model.Payment;
import com.bluebell.project.repository.BookingRepository;
import com.bluebell.project.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;

    public PaymentService(PaymentRepository paymentRepo, BookingRepository bookingRepo) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
    }

    public List<PaymentDto> listAll() {
        return paymentRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByBookingId(Long bookingId) {
        return paymentRepo.findByBooking_Id(bookingId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByBookingCode(String bookingCode) {
        return paymentRepo.findByBooking_BookingCode(bookingCode).stream()
                .map(this::toDto)
                .toList();
    }

    public PaymentDto getById(Long id) {
        return paymentRepo.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    @Transactional
    public PaymentDto createPayment(Long bookingId, PaymentCreateRequest req) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + bookingId));

        Payment payment = new Payment();
        payment.setAmount(req.getAmount());
        payment.setMethod(req.getPaymentMethod());
        payment.setPaymentDate(req.getPaymentDate() != null ? req.getPaymentDate() : LocalDateTime.now());
        payment.setBooking(booking);

        Payment saved = paymentRepo.save(payment);
        return toDto(saved);
    }

    @Transactional
    public PaymentDto updatePayment(Long id, PaymentCreateRequest req) {
        Payment existing = paymentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id " + id));

        existing.setAmount(req.getAmount());
        existing.setMethod(req.getPaymentMethod());
        existing.setPaymentDate(req.getPaymentDate());

        Payment saved = paymentRepo.save(existing);
        return toDto(saved);
    }

    @Transactional
    public void deletePayment(Long id) {
        paymentRepo.deleteById(id);
    }

    // 🔹 Helper mapper
    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getBooking().getBookingCode(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getPaymentDate()
        );
    }
}
