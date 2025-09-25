package com.bluebell.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_information")
public class CustomerInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    private String contactNumber;

    private String gender;

    // Optional: if you want to link directly with booking later
    // @OneToOne(mappedBy = "customerInformation")
    // private Booking booking;

    public CustomerInformation() {}

    public CustomerInformation(String fullname, String email, String contactNumber) {
        this.fullname = fullname;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
