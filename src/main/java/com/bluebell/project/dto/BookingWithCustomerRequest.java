package com.bluebell.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class BookingWithCustomerRequest {

    @JsonProperty("bookingId")
    private Long bookingId;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int adults;
    private int kids;
    private String unitType;

    private CustomerDto customer;

    // Inner DTO for customer
    public static class CustomerDto {
        private Long id;
        private String fullname;
        private String email;
        private String contactNumber;

        // getters & setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getFullname() { return fullname; }
        public void setFullname(String fullname) { this.fullname = fullname; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    }

    // getters & setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public int getAdults() { return adults; }
    public void setAdults(int adults) { this.adults = adults; }

    public int getKids() { return kids; }
    public void setKids(int kids) { this.kids = kids; }

    public String getUnitType() { return unitType; }
    public void setUnitType(String unitType) { this.unitType = unitType; }

    public CustomerDto getCustomer() { return customer; }
    public void setCustomer(CustomerDto customer) { this.customer = customer; }
}
