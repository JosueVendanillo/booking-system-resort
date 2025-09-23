package com.bluebell.project.dto;

public class CustomerInformationDto {

    private Long id;
    private String fullname;
    private String email;
    private String contactNumber;

    public CustomerInformationDto() {}

    public CustomerInformationDto(Long id, String fullname, String email, String contactNumber) {
        this.id = id;
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
}
