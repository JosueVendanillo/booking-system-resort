package com.bluebell.project.service;

import com.bluebell.project.config.BookingConfig;
import com.bluebell.project.config.EntrancePricesConfig;
import com.bluebell.project.dto.BookingSummaryDto;
import com.bluebell.project.model.CustomerInformation;
import com.bluebell.project.repository.BookingRepository;
import com.bluebell.project.dto.BookingCreateRequest;
import com.bluebell.project.dto.BookingDto;
import com.bluebell.project.dto.BookingUpdateRequest;
import com.bluebell.project.model.Booking;
import com.bluebell.project.repository.RoomInventoryRepository;
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

    private final EntrancePricesConfig entrancePricesConfig;

    private final RoomInventoryService roomInventoryService;

    private final RoomInventoryRepository roomInventoryRepository;

    public BookingService(BookingRepository repo, BookingConfig config, EntrancePricesConfig entrancePricesConfig, RoomInventoryService roomInventoryService, RoomInventoryRepository roomInventoryRepository) {
        this.repo = repo;
        this.config = config;
        this.entrancePricesConfig = entrancePricesConfig;
        this.roomInventoryService = roomInventoryService;
        this.roomInventoryRepository = roomInventoryRepository;
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

    private void validateRoomCapacity(BookingCreateRequest request) {
        String roomType = request.getUnitType();
        int roomCapacity = roomInventoryRepository.checkRoomCapacity(roomType);
        int paxCount = request.getAdults() + request.getKids();

        System.out.println("ROOM TYPE: " +  roomType);
        System.out.println("MAX CAPACITY: " + roomCapacity);
        System.out.println("PAX COUNT: " + paxCount);

        if (paxCount > roomCapacity) {
            throw new IllegalArgumentException(
                    String.format("Room capacity exceeded for type '%s'. Max: %d, Requested: %d",
                            roomType, roomCapacity, paxCount)
            );
        }

        System.out.println("Pax per room validated successfully.");
    }




    @Transactional
    public BookingDto create(BookingCreateRequest req) {
        validateDateOrder(req.getCheckIn(), req.getCheckOut());
        preventOverlap(req.getUnitType(), req.getCheckIn(), req.getCheckOut(), null);

        showRequestDateTime(req);


        double kidPrice = req.getKids() * entrancePricesConfig.getKidsPrice();
        double adultPrice = req.getAdults() * entrancePricesConfig.getAdultPrice();

        long noOfDays = Duration.between(req.getCheckIn(), req.getCheckOut()).toDays();
        if (noOfDays == 0) noOfDays = 1; // Ensure same-day bookings count as 1 day
        System.out.println("LENGTH OF DAYS: " + noOfDays);
        double totalAmount = calculatePrice(req.getUnitType(), noOfDays);
        double finalTotalAmount = totalAmount + adultPrice + kidPrice;

        System.out.println("Room Price: " + totalAmount);
        System.out.println("Adult Price: " + adultPrice);
        System.out.println("Kid(s) Price: " + kidPrice);
        System.out.println("Final Total Amount: " + finalTotalAmount);


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
        b.setBookStatus("PENDING");
        b.setAddOns(req.getAddOns());
//        b.setLeisureTime(req.getLeisureTime);

        // Keep totalAmount from frontend for now (can later calculate by pricing rules)
        b.setTotalAmount(finalTotalAmount);
        b.setPaymentStatus("PENDING");
        b.setCustomer(ci);

        ci.setFullname(req.getFullname()); // revisit this
        ci.setEmail(req.getCustomer().getEmail());
        ci.setContactNumber(req.getCustomer().getContactNumber());
        ci.setGender(req.getCustomer().getGender());
        ci.setCreatedBy(req.getCustomer().getCreatedBy());

        Booking saved = repo.save(b);
        return toDto(saved);
    }

    @Transactional
    public BookingDto update(Long id, BookingUpdateRequest req) {
        Booking existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateDateOrder(req.getCheckIn(), req.getCheckOut());
        preventOverlap(req.getUnitType(), req.getCheckIn(), req.getCheckOut(), id);


        double kidPrice = req.getKids() * entrancePricesConfig.getKidsPrice();
        double adultPrice = req.getAdults() * entrancePricesConfig.getAdultPrice();

        long noOfDays = java.time.Duration.between(req.getCheckIn(), req.getCheckOut()).toDays();
        if(noOfDays == 0) noOfDays = 1;
        double totalAmount = calculatePrice(req.getUnitType(), noOfDays);
        double finalTotalAmount = totalAmount + adultPrice + kidPrice;

        System.out.println("UPDATE ============================================");
        System.out.println("Room Price: " + totalAmount);
        System.out.println("Adult Price: " + adultPrice);
        System.out.println("Kid(s) Price: " + kidPrice);
        System.out.println("Final Total Amount: " + finalTotalAmount);

        existing.setFullname(req.getFullname());
        existing.setAdults(req.getAdults());
        existing.setKids(req.getKids());
        existing.setUnitType(req.getUnitType());
        existing.setCheckIn(req.getCheckIn());
        existing.setCheckOut(req.getCheckOut());
        existing.setNoOfDays((int) noOfDays);
        existing.setTotalAmount(finalTotalAmount);
//        existing.setAddOns(req.getAddOns());
//        existing.setLeisureTime(req.getLeisureTime());
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

//        Transfer this to update()
//        if (!checkOut.isAfter(checkIn)) throw new IllegalArgumentException("checkOut must be after checkIn");
//        if (checkIn.isBefore(LocalDateTime.now().withSecond(0).withNano(0))) throw new IllegalArgumentException("checkIn cannot be in the past");
    }

    private void preventOverlap(String unitType, LocalDateTime checkIn, LocalDateTime checkOut, Long excludeId) {
        List<Booking> overlaps = repo.findOverlapping(unitType, checkIn, checkOut, excludeId);

        // How many rooms of this type exist
//        int availableRooms = roomInventoryService.getAvailableRooms(
//                unitType.trim().toLowerCase().replace(" ", "-")
//        );

                int availableRooms = roomInventoryService.getTotalRooms(
                unitType.trim().toLowerCase().replace(" ", "-")
        );

        System.out.println("No. of Available Room(s) is: " + availableRooms + " for " + "unitType: " + unitType);

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
        d.setBookStatus(b.getBookStatus());
        d.setLeisureTime(b.getLeisureTime());
        d.setAddOns(b.getAddOns());
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
        System.out.println("Book Status: " + request.getBookStatus());
        System.out.println("Leisure Time: " + request.getLeisureTime());
        System.out.println("Add-Ons: " + request.getAddOns());

        if (request.getCustomer() != null) {
            System.out.println("Customer Name: " + request.getCustomer().getFullname());
            System.out.println("Email        : " + request.getCustomer().getEmail());
            System.out.println("Gender  : " + request.getCustomer().getGender());
            System.out.println("Contact No   : " + request.getCustomer().getContactNumber());
            System.out.println("Created By:  : " + request.getCustomer().getCreatedBy());
        } else {
            System.out.println("Customer     : null");
        }

        System.out.println("======================================");
    }


    public Long getTotalBookingsThisYear() {
        return repo.countBookingsThisYear();
    }


    public void showAllRoomCapacities() {
        List<Object[]> result = roomInventoryRepository.findAllRoomTypesAndCapacities();

        for (Object[] row : result) {
            String roomType = (String) row[0];
            Integer roomMaxCapacity = (Integer) row[1];

            System.out.println("Room Type: " + roomType + ", Max Capacity: " + roomMaxCapacity);
        }
    }

    public void showAllRoomAvailability() {
        List<Object[]> result = roomInventoryRepository.getRoomAvailability();

        for (Object[] row : result) {
            String roomType = (String) row[0];
            Integer totalRoomCapacity = (Integer) row[1];
            Integer availableRoomCapacity = (Integer) row[2];

            System.out.println("Room Type: " + roomType + ", Max Room Capacity: " + totalRoomCapacity + ", Available Room Count" + availableRoomCapacity);
        }
    }


    // ✅ Convenience methods for clarity and cleaner API calls
    public BookingDto manualCheckin(BookingCreateRequest request) {
        return updateBookingStatus(request, "CHECKED-IN");
    }

    public BookingDto manualCheckOut(BookingCreateRequest request) {
        return updateBookingStatus(request, "CHECKED-OUT");
    }

    public BookingDto cancelBooking(BookingCreateRequest request) {
        return updateBookingStatus(request, "CANCELED");
    }

    // 🔹 Centralized logic for updating booking status
    private BookingDto updateBookingStatus(BookingCreateRequest request, String status) {

        // 🧩 Find the existing booking by booking code
        Booking booking = repo.findByBookingCode(request.getBookingCode())
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + request.getBookingCode()));

        switch (status) {
            case "CHECKED-IN":
                System.out.println("CHECK-IN TIME: " + request.getCheckIn());
                booking.setCheckIn(request.getCheckIn());
                break;

            case "CHECKED-OUT":
                System.out.println("CHECK-OUT TIME: " + request.getCheckOut());
                booking.setCheckOut(request.getCheckOut());
                break;

            case "CANCELED":
                System.out.println("BOOKING CANCELED: " + request.getBookStatus());
                break;

            default:
                throw new IllegalArgumentException("Invalid booking status: " + status);
        }

        // ✅ Update status and save
        booking.setBookStatus(status);
        Booking saved = repo.save(booking);

        return toDto2(saved);
    }

    // ✅ Convert Entity → DTO
    private BookingDto toDto2(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setBookingCode(booking.getBookingCode());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setBookStatus(booking.getBookStatus());
        return dto;
    }


    public List<BookingSummaryDto> getBookingSummaryByFullName(String fullName) {
        return repo.findBookingSummaryByFullName(fullName);
    }

}
