package com.bluebell.project.service;

import com.bluebell.project.config.BookingConfig;
import com.bluebell.project.dto.BookingCreateRequest;
import com.bluebell.project.dto.BookingWithCustomerRequest;
import com.bluebell.project.model.Booking;
import com.bluebell.project.model.CustomerInformation;
import com.bluebell.project.repository.BookingRepository;
import com.bluebell.project.repository.CustomerInformationRepository;
import com.bluebell.project.util.BookingIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class BookingWithCustomerService {

    private final BookingConfig config;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private CustomerInformationRepository customerRepo;


    public BookingWithCustomerService(BookingConfig config, BookingRepository bookingRepo, CustomerInformationRepository customerRepo) {
        this.config = config;
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
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


    public Booking createBookingWithCustomer(BookingWithCustomerRequest request) {

        showRequestDateTime(request);

        long noOfDays = Duration.between(request.getCheckIn(), request.getCheckOut()).toDays();
        if (noOfDays == 0) noOfDays = 1; // Ensure same-day bookings count as 1 day
        Double totalAmount = (double) calculatePrice(request.getUnitType(), noOfDays);

        Booking booking = new Booking();

        booking.setBookingCode(booking.getBookingCode());
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setAdults(request.getAdults());
        booking.setKids(request.getKids());
        booking.setUnitType(request.getUnitType());
        booking.setFullname(request.getCustomer().getFullname());
        booking.setNoOfDays((int) noOfDays);
        // Keep totalAmount from frontend for now (can later calculate by pricing rules)
        booking.setTotalAmount(totalAmount);
        booking.setPaymentStatus("PENDING");
        // Map customer
//        BookingWithCustomerRequest.CustomerDto custDto = request.getCustomer();

        CustomerInformation customer = new CustomerInformation();
//        customer.setId(custDto.getId());
        customer.setFullname(request.getCustomer().getFullname());
        customer.setEmail(request.getCustomer().getEmail());
        customer.setContactNumber(request.getCustomer().getContactNumber());

        CustomerInformation savedCustomer = customerRepo.save(customer);
        booking.setCustomer(savedCustomer);

        return bookingRepo.save(booking);
    }


    public Optional<Booking> getBookingWithCustomer(Long id) {
        return bookingRepo.findById(id);
    }

    public List<Booking> getAllBookingsWithCustomer() {
        return bookingRepo.findAll();
    }

    public void showRequestDateTime(BookingWithCustomerRequest request) {
        if (request == null) {
            System.out.println("BookingCreateRequest is null");
            return;
        }

        System.out.println("==== BookingCreateRequest with Customer Details ====");
        System.out.println("Fullname     : " + request.getCustomer().getFullname());
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


}
