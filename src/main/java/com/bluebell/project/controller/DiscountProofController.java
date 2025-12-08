package com.bluebell.project.controller;

import com.bluebell.project.model.DiscountProof;
import com.bluebell.project.repository.DiscountProofRepository;
import com.bluebell.project.service.DiscountProofService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/uploads")

public class DiscountProofController {

    private final DiscountProofService service;
    private final DiscountProofRepository discountProofRepository;

    public DiscountProofController(DiscountProofService service, DiscountProofRepository discountProofRepository) {
        this.service = service;
        this.discountProofRepository = discountProofRepository;
    }

    @PostMapping("/discount-proofs")
    public ResponseEntity<?> uploadProof(
            @RequestParam("bookingId") String bookingId,
            @RequestParam("discountType") String discountType,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestParam("files") MultipartFile[] files) {

        try {
            String folder = "uploads/discount-proofs/";
            Files.createDirectories(Paths.get(folder));

            List<DiscountProof> savedProofs = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(folder + fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                DiscountProof proof = new DiscountProof();
                proof.setBookingId(bookingId);
                proof.setDiscountType(discountType);
                proof.setFileName(fileName);
                proof.setFilePath(filePath.toString());
                proof.setUploadedBy(uploadedBy);
                proof.setUploadedAt(LocalDateTime.now());

                discountProofRepository.save(proof);
                savedProofs.add(proof);
            }

            return ResponseEntity.ok(savedProofs);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }


    @GetMapping("/discount-proofs/by-uploader/{uploadedBy}")
    public ResponseEntity<?> getProofsByUploader(@PathVariable String uploadedBy) {
        try {
            List<DiscountProof> proofs = service.getProofsByUploader(uploadedBy);

            if (proofs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(proofs);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error fetching proofs: " + e.getMessage());
        }
    }


}
