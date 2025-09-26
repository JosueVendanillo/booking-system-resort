//package com.bluebell.project.service;
//
//import com.bluebell.project.dto.PaymentCreateRequest;
//import com.bluebell.project.dto.PaymentDto;
//import com.bluebell.project.model.Booking;
//import com.bluebell.project.repository.BookingRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class PaymentServiceTest {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @Autowired
//    private BookingRepository bookingRepo;
//
//    @Test
//    public void testFirstPaymentMustBe30Percent() {
//        Booking booking = new Booking();
//        booking.setBookingCode("TEST001");
//        booking.setTotalAmount(10000.0);
//        booking.setPaymentStatus("PENDING");
//        booking.setUnitType("Deluxe");
//        bookingRepo.save(booking);
//
//        PaymentCreateRequest wrongRequest = new PaymentCreateRequest();
//        wrongRequest.setBookingCode("TEST001");
//        wrongRequest.setAmount(2000.0); // ❌ Wrong
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            paymentService.createPayment(wrongRequest);
//        });
//
//        PaymentCreateRequest correctRequest = new PaymentCreateRequest();
//        correctRequest.setBookingCode("TEST001");
//        correctRequest.setAmount(3000.0); // ✅ Correct
//        correctRequest.setPaymentMethod("CREDIT_CARD");
//        correctRequest.setPaymentDate(LocalDateTime.from(LocalDate.now()));
//
//        PaymentDto dto = paymentService.createPayment(correctRequest);
//        assertEquals(3000.0, dto.getAmount());
//    }
//}
