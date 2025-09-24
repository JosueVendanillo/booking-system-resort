package com.bluebell.project.service;

import com.bluebell.project.dto.PaymentCreateRequest;
import com.bluebell.project.dto.PaymentDto;
import com.bluebell.project.model.Booking;
import com.bluebell.project.model.Payment;
import com.bluebell.project.repository.BookingRepository;
import com.bluebell.project.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    //  Create a new payment
    public PaymentDto createPayment(PaymentCreateRequest request) {
        logger.info(" Creating payment for bookingCode={} with amount={} and method={}",
                request.getBookingCode(), request.getAmount(), request.getPaymentMethod());

        // Find booking by bookingCode
        Booking booking = bookingRepo.findByBookingCode(request.getBookingCode())
                .orElseThrow(() -> {
                    logger.error(" Booking not found with code={}", request.getBookingCode());
                    return new RuntimeException("Booking not found with code: " + request.getBookingCode());
                });

        logger.debug(" Found booking id={} totalAmount={}", booking.getId(), booking.getTotalAmount());

        // Validate amount (must match booking total)
        if (Math.abs(booking.getTotalAmount() - request.getAmount()) > 0.01) {
            logger.warn("⚠ Payment validation failed: expectedAmount={} but got={}",
                    booking.getTotalAmount(), request.getAmount());
            throw new IllegalArgumentException("Payment amount does not match booking total.");
        }

        // Create Payment entity
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setStatus("COMPLETED");

        logger.debug(" Saving payment entity: {}", payment);

        // Save payment
        Payment saved = paymentRepo.save(payment);

        logger.info(" Payment saved successfully with id={} for bookingCode={}",
                saved.getId(), booking.getBookingCode());

        // Update booking status
        booking.setPaymentStatus("PAID");
        bookingRepo.save(booking);

        logger.info(" Booking id={} updated to paymentStatus=PAID", booking.getId());

        return toDto(saved);
    }

    //  Get all payments
    public List<PaymentDto> getAllPayments() {
        return paymentRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    //  Get payment by ID
    public PaymentDto getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepo.findById(id);
        return payment.map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    //  Get payments by booking ID
    public List<PaymentDto> getPaymentsByBookingId(Long bookingId) {
        return paymentRepo.findByBooking_Id(bookingId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //  Get payments by booking code
    public List<PaymentDto> getPaymentsByBookingCode(String bookingCode) {
        return paymentRepo.findByBooking_BookingCode(bookingCode)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //  Convert Entity → DTO
    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getBooking().getBookingCode(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentDate(),
                payment.getStatus()
        );
    }
}
