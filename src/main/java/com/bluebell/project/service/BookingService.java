package com.bluebell.project.service;

import com.bluebell.project.config.BookingConfig;
import com.bluebell.project.model.CustomerInformation;
import com.bluebell.project.repository.BookingRepository;
import com.bluebell.project.dto.BookingCreateRequest;
import com.bluebell.project.dto.BookingDto;
import com.bluebell.project.dto.BookingUpdateRequest;
import com.bluebell.project.model.Booking;
import com.bluebell.project.util.BookingIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    String bookingId = BookingIdGenerator.generate(); // e.g. BKG-20250923-A1B2C3

    private final BookingRepository repo;
    private final BookingConfig config;

    private final RoomInventoryService roomInventoryService;

    public BookingService(BookingRepository repo, BookingConfig config, RoomInventoryService roomInventoryService) {
        this.repo = repo;
        this.config = config;
        this.roomInventoryService = roomInventoryService;
    }

//    private int calculatePrice(String unitType, long noOfDays) {
//        return config.getPrices().getOrDefault(unitType, 0) * (int) noOfDays;
//    }



    private int calculatePrice(String unitType, long noOfDays) {
        // normalize (e.g. "Family Room" -> "family-room")
        String normalized = unitType.trim().toLowerCase().replace(" ", "-");
        Integer pricePerDay = config.getPrices().get(normalized);

        System.out.println("UnitType: " + unitType
                + " | Normalized: " + normalized
                + " | PricePerDay: " + pricePerDay
                + " | Days: " + noOfDays);

        return (pricePerDay != null ? pricePerDay : 0) * (int) noOfDays;
    }


    public List<BookingDto> listAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public BookingDto getById(Long id) {
        return repo.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public BookingDto create(BookingCreateRequest req) {
        validateDateOrder(req.getCheckIn(), req.getCheckOut());
        preventOverlap(req.getUnitType(), req.getCheckIn(), req.getCheckOut(), null);

        showRequestDateTime(req);

        long noOfDays = Duration.between(req.getCheckIn(), req.getCheckOut()).toDays();
        if (noOfDays == 0) noOfDays = 1; // Ensure same-day bookings count as 1 day
        System.out.println("LENGTH OF DAYS: " + noOfDays);
        Double totalAmount = (double) calculatePrice(req.getUnitType(), noOfDays);

        Booking b = new Booking();
        CustomerInformation ci = new CustomerInformation();

        b.setBookingCode(b.getBookingCode());
        b.setFullname(req.getFullname());
        b.setAdults(req.getAdults());
        b.setKids(req.getKids());
        b.setUnitType(req.getUnitType());
        b.setCheckIn(req.getCheckIn());
        b.setCheckOut(req.getCheckOut());
        b.setNoOfDays((int) noOfDays);

        // Keep totalAmount from frontend for now (can later calculate by pricing rules)
        b.setTotalAmount(totalAmount);
        b.setPaymentStatus("PENDING");
        b.setCustomer(ci);

        ci.setFullname(req.getFullname()); // revisit this
        ci.setEmail(req.getCustomer().getEmail());
        ci.setContactNumber(req.getCustomer().getContactNumber());
        ci.setGender(req.getCustomer().getGender());

        Booking saved = repo.save(b);
        return toDto(saved);
    }

    @Transactional
    public BookingDto update(Long id, BookingUpdateRequest req) {
        Booking existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateDateOrder(req.getCheckIn(), req.getCheckOut());
        preventOverlap(req.getUnitType(), req.getCheckIn(), req.getCheckOut(), id);

        long noOfDays = java.time.Duration.between(req.getCheckIn(), req.getCheckOut()).toDays();
        if(noOfDays == 0) noOfDays = 1;
        Double totalAmount = (double) calculatePrice(req.getUnitType(), noOfDays);

        existing.setFullname(req.getFullname());
        existing.setAdults(req.getAdults());
        existing.setKids(req.getKids());
        existing.setUnitType(req.getUnitType());
        existing.setCheckIn(req.getCheckIn());
        existing.setCheckOut(req.getCheckOut());
        existing.setNoOfDays((int) noOfDays);
        existing.setTotalAmount(totalAmount);

        Booking saved = repo.save(existing);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private void validateDateOrder(LocalDateTime checkIn, LocalDateTime checkOut) {

        System.out.println("CHECKIN VALUE: " + checkIn);
        System.out.println("CHECKOUT VALUE: " + checkOut);
        if (checkIn == null || checkOut == null) throw new IllegalArgumentException("Dates required");
        if (!checkOut.isAfter(checkIn)) throw new IllegalArgumentException("checkOut must be after checkIn");
        if (checkIn.isBefore(LocalDateTime.now().withSecond(0).withNano(0))) throw new IllegalArgumentException("checkIn cannot be in the past");
    }

    private void preventOverlap(String unitType, LocalDateTime checkIn, LocalDateTime checkOut, Long excludeId) {
        List<Booking> overlaps = repo.findOverlapping(unitType, checkIn, checkOut, excludeId);

        // How many rooms of this type exist
        int availableRooms = roomInventoryService.getAvailableRooms(
                unitType.trim().toLowerCase().replace(" ", "-")
        );

        if (overlaps.size() >= availableRooms) {
            throw new IllegalArgumentException(
                    "No more rooms available for type: " + unitType + " in the selected date range."
            );
        }
    }


    private BookingDto toDto(Booking b) {
        BookingDto d = new BookingDto();
        d.setId(b.getId());
        d.setBookingCode(b.getBookingCode());
        d.setFullname(b.getFullname());
        d.setAdults(b.getAdults());
        d.setKids(b.getKids());
        d.setUnitType(b.getUnitType());
        d.setCheckIn(b.getCheckIn());
        d.setCheckOut(b.getCheckOut());
        d.setNoOfDays(b.getNoOfDays());
        d.setTotalAmount(b.getTotalAmount());
        d.setPaymentStatus(b.getPaymentStatus());
        return d;
    }

    public BookingDto getByCode(String bookingCode) {
        return repo.findByBookingCode(bookingCode)
                .map(this::toDto)
                .orElse(null);
    }

    public void showRequestDateTime(BookingCreateRequest request) {
        if (request == null) {
            System.out.println("BookingCreateRequest is null");
            return;
        }

        System.out.println("==== BookingCreateRequest Details ====");
        System.out.println("Fullname     : " + request.getFullname());
        System.out.println("Adults       : " + request.getAdults());
        System.out.println("Kids         : " + request.getKids());
        System.out.println("Unit Type    : " + request.getUnitType());
        System.out.println("Check In     : " + request.getCheckIn());
        System.out.println("Check Out    : " + request.getCheckOut());

        if (request.getCustomer() != null) {
            System.out.println("Customer Name: " + request.getCustomer().getFullname());
            System.out.println("Email        : " + request.getCustomer().getEmail());
            System.out.println("Contact No   : " + request.getCustomer().getContactNumber());
        } else {
            System.out.println("Customer     : null");
        }

        System.out.println("======================================");
    }


    public Long getTotalBookingsThisYear() {
        return repo.countBookingsThisYear();
    }




}
