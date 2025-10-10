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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RoomInventoryService roomInventoryService;

    //  Create a new payment
    @Transactional
    public PaymentDto createPayment(PaymentCreateRequest request) {
        logger.info(" Creating payment for bookingCode={} with amount={} and method={}",
                request.getBookingCode(), request.getAmount(), request.getPaymentMethod());

        // Find booking by bookingCode
        Booking booking = bookingRepo.findByBookingCode(request.getBookingCode())
                .orElseThrow(() -> {
                    logger.error(" Booking not found with code={}", request.getBookingCode());
                    return new RuntimeException("Booking not found with code: " + request.getBookingCode());
                });

        logger.debug(" Found booking id={} totalAmount={} currentPaymentStatus={}",
                booking.getId(), booking.getTotalAmount(), booking.getPaymentStatus());

        double totalAmount = booking.getTotalAmount();
        double downPaymentRequired = totalAmount * 0.30;

        // Fetch existing payments for this booking
        List<Payment> existingPayments = paymentRepo.findByBooking_Id(booking.getId());
        double alreadyPaid = existingPayments.stream().mapToDouble(Payment::getAmount).sum();
        double remainingBalance = totalAmount - alreadyPaid;

        // 🔹 First payment must be exactly 30%
        if (existingPayments.isEmpty()) {
            if (Math.abs(request.getAmount() - downPaymentRequired) > 0.01) {
                logger.warn(" Initial payment validation failed: expected={} but got={}",
                        downPaymentRequired, request.getAmount());
                throw new IllegalArgumentException("Initial payment must be exactly 30% of total (" + downPaymentRequired + ")");
            }
            booking.setPaymentStatus("PARTIALLY_PAID");
        } else {
            // 🔹 Subsequent payments must NOT exceed remaining balance
            if (request.getAmount() > remainingBalance + 0.01) {
                logger.warn(" Payment exceeds remaining balance: remaining={} but got={}",
                        remainingBalance, request.getAmount());
                throw new IllegalArgumentException("Payment exceeds remaining balance (" + remainingBalance + ")");
            }

            // Update status depending on new balance
            if (Math.abs(request.getAmount() - remainingBalance) < 0.01) {
                booking.setPaymentStatus("PAID");
            } else {
                booking.setPaymentStatus("PARTIALLY_PAID");
            }
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

        // Save updated booking
        bookingRepo.save(booking);
        logger.info(" Booking id={} updated to paymentStatus={} (after payment of {})",
                booking.getId(), booking.getPaymentStatus(), request.getAmount());

        // Deduct room availability only when fully paid
        if ("PAID".equalsIgnoreCase(booking.getPaymentStatus())) {
            try {
                String normalizedRoom = booking.getUnitType().trim().toLowerCase().replace(" ", "-");
                roomInventoryService.decreaseAvailability(normalizedRoom, 1);
                logger.info(" Room availability updated for type={} (decreased by 1)", normalizedRoom);
            } catch (Exception e) {
                logger.error(" Failed to update room availability: {}", e.getMessage());
                throw new RuntimeException("Payment succeeded, but failed to update room availability.");
            }
        }

        return toDto(saved);
    }

    @Transactional
    public PaymentDto createPaymentFromHomePage(PaymentCreateRequest request) {
        logger.info(" Creating payment for bookingCode={} with amount={} and method={}",
                request.getBookingCode(), request.getAmount(), request.getPaymentMethod());

        Booking booking = bookingRepo.findByBookingCode(request.getBookingCode())
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + request.getBookingCode()));


        double totalAmount = booking.getTotalAmount();
        double downPaymentRequired = totalAmount * 0.30;
        System.out.println("TOTAL AMOUNT: " + totalAmount);
        System.out.println("GET AMOUNT: " + request.getAmount());
        System.out.println("DOWNPAYMENT: " + downPaymentRequired);


        // Fetch existing payments for this booking
        List<Payment> existingPayments = paymentRepo.findByBooking_Id(booking.getId());
        double alreadyPaid = existingPayments.stream().mapToDouble(Payment::getAmount).sum();
        double remainingBalance = totalAmount - alreadyPaid;
        System.out.println("ALREADY PAID: " + alreadyPaid);

        System.out.println("BALANCE: " + remainingBalance);

        // 🔹 First payment must be exactly 30%
        if (existingPayments.isEmpty()) {
            if (Math.abs(request.getAmount() - downPaymentRequired) > 0.01) {
                logger.warn(" Initial payment validation failed: expected={} but got={}",
                            downPaymentRequired, request.getAmount());
                throw new IllegalArgumentException("Initial payment must be exactly 30% of total (" + downPaymentRequired + ")");
            }
            booking.setPaymentStatus("PARTIALLY_PAID");
        } else {
            // 🔹 Subsequent payments must NOT exceed remaining balance
            if (request.getAmount() > remainingBalance + 0.01) {
                logger.warn(" Payment exceeds remaining balance: remaining={} but got={}",
                        remainingBalance, request.getAmount());
                throw new IllegalArgumentException("Payment exceeds remaining balance (" + remainingBalance + ")");
            }

            // Update status depending on new balance
            if (Math.abs(request.getAmount() - remainingBalance) < 0.01) {
                booking.setPaymentStatus("PAID");
            } else {
                booking.setPaymentStatus("PARTIALLY_PAID");
            }
        }


        // Create Payment entity
        Payment payment = new Payment();

        String dpPaymentStatus = "COMPLETED";

        payment.setBooking(booking);
        payment.setBookingCode(request.getBookingCode());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setStatus(dpPaymentStatus);

        System.out.println("PAYMENT:=====");
        System.out.println("BOOKING CODE: " + payment.getBookingCode());
        System.out.println("AMOUNT: " + payment.getAmount());
        System.out.println("PAYMENT METHOD: " + payment.getPaymentMethod());
        System.out.println("PAYMENT DATE: " + payment.getPaymentDate());
        System.out.println("PAYMENT STATUS: " + payment.getStatus());

        logger.debug(" Saving payment entity: {}", payment);

        // Save payment
        Payment saved = paymentRepo.save(payment);

        // Deduct room availability only when fully paid

            try {
                String normalizedRoom = booking.getUnitType().trim().toLowerCase().replace(" ", "-");
                roomInventoryService.decreaseAvailability(normalizedRoom, 1);
                logger.info(" Room availability updated for type={} (decreased by 1)", normalizedRoom);
            } catch (Exception e) {
                logger.error(" Failed to update room availability: {}", e.getMessage());
                throw new RuntimeException("Payment succeeded, but failed to update room availability.");
            }

        return toDto(saved);
    }


    @Transactional
    public PaymentDto completeRemainingPayment(String bookingCode, String paymentMethod) {
        Booking booking = bookingRepo.findByBookingCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingCode));

        List<Payment> existingPayments = paymentRepo.findByBooking_Id(booking.getId());
        double alreadyPaid = existingPayments.stream().mapToDouble(Payment::getAmount).sum();
        double remainingBalance = booking.getTotalAmount() - alreadyPaid;

        if (remainingBalance <= 0.01) {
            throw new IllegalArgumentException("Booking is already fully paid.");
        }

        // Create a payment for remaining balance
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(remainingBalance);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(java.time.LocalDateTime.now());
        payment.setStatus("COMPLETED");

        Payment saved = paymentRepo.save(payment);

        // Update booking status
        booking.setPaymentStatus("PAID");
        bookingRepo.save(booking);

        // Deduct room availability
        try {
            String normalizedRoom = booking.getUnitType().trim().toLowerCase().replace(" ", "-");
            roomInventoryService.decreaseAvailability(normalizedRoom, 1);
        } catch (Exception e) {
            throw new RuntimeException("Payment succeeded, but failed to update room availability.");
        }
        double updatedRemainingBalance = 0.0;

        return new PaymentDto(
                saved.getId(),
                booking.getBookingCode(),
                saved.getAmount(),
                saved.getPaymentMethod(),
                saved.getPaymentDate(),
                saved.getStatus(),
                updatedRemainingBalance
        );
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

        Booking booking = payment.getBooking();

        // calculate total already paid including this payment
        double alreadyPaid = booking.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        double remainingBalance = booking.getTotalAmount() - alreadyPaid;

        return new PaymentDto(
                payment.getId(),
                payment.getBooking().getBookingCode(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentDate(),
                payment.getStatus(),
                remainingBalance);
    }


    public Long getTotalBills() {
        return paymentRepo.countTotalBills();
    }


//
//    @Transactional
//    public void confirmBookingPayment(Long bookingId) {
//        Booking booking = bookingRepo.findById(bookingId)
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
//
//        if (!"PAID".equalsIgnoreCase(booking.getPaymentStatus())) {
//            booking.setPaymentStatus("PAID");
//            bookingRepo.save(booking);
//
//            // Update room availability
//            String normalizedRoom = booking.getUnitType().trim().toLowerCase().replace(" ", "-");
//            int currentAvailable = roomInventoryService.getAvailableRooms(normalizedRoom);
//            if (currentAvailable > 0) {
//                roomInventoryService.updateAvailability(normalizedRoom, currentAvailable - 1);
//            } else {
//                throw new IllegalStateException("No more rooms available for type: " + booking.getUnitType());
//            }
//        }
//    }
}
