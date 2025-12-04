package com.bluebell.project.model;

import com.bluebell.project.util.BookingIdGenerator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "bookings",
        indexes = {@Index(name = "idx_unit_checkin_checkout", columnList = "unit_type, check_in, check_out")}
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_code", nullable = false, unique = true, length = 30)
    private String bookingCode;


    @Column(name = "fullname", nullable = false, length = 255)
    private String fullname;

    @Column(name = "adults", nullable = false)
    private Integer adults;

    @Column(name = "kids", nullable = false)
    private Integer kids;

    @Column(name = "unit_type", nullable = false, length = 100)
    private String unitType;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @Column(name = "no_of_days", nullable = false)
    private Integer noOfDays;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus = "PENDING";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "book_status", nullable = false)
    private String bookStatus = "PENDING";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private CustomerInformation customer;

    // 🔑 New: list of payments linked to this booking
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "leisure_Time") // day or night
    private String LeisureTime;

    @Column(name = "add_ons")
    private String addOns;

    public Booking() {}

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (paymentStatus == null) paymentStatus = "PENDING";
        if (bookingCode == null || bookingCode.isBlank()) {
            bookingCode = BookingIdGenerator.generate(); // use the utility class
        }
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public Integer getAdults() { return adults; }
    public void setAdults(Integer adults) { this.adults = adults; }

    public Integer getKids() { return kids; }
    public void setKids(Integer kids) { this.kids = kids; }

    public String getUnitType() { return unitType; }
    public void setUnitType(String unitType) { this.unitType = unitType; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getNoOfDays() { return noOfDays; }
    public void setNoOfDays(Integer noOfDays) { this.noOfDays = noOfDays; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public CustomerInformation getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInformation customer) {
        this.customer = customer;
    }


    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getLeisureTime() {
        return LeisureTime;
    }

    public void setLeisureTime(String leisureTime) {
        LeisureTime = leisureTime;
    }

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }
}
