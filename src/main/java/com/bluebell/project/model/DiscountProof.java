package com.bluebell.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "discount_proofs")
public class DiscountProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;

    private String discountType;

    private String fileName;

    private String filePath;

    private LocalDateTime uploadedAt;

    private String uploadedBy; //fullname

    public DiscountProof() {}

    public DiscountProof(String bookingId, String discountType, String fileName, String filePath) {
        this.bookingId = bookingId;
        this.discountType = discountType;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadedAt = LocalDateTime.now();
    }

    // GETTERS & SETTERS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }


    //    public Long getBookingId() {
//        return bookingId;
//    }
//
//    public void setBookingId(Long bookingId) {
//        this.bookingId = bookingId;
//    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
