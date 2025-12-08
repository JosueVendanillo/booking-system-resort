package com.bluebell.project.service;

import com.bluebell.project.model.DiscountProof;
import com.bluebell.project.repository.DiscountProofRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DiscountProofService {

    @Value("${upload.path:uploads/discount-proofs}")
    private String uploadDir;

    private final DiscountProofRepository repository;

    public DiscountProofService(DiscountProofRepository repository) {
        this.repository = repository;
    }



    public List<DiscountProof> getProofsByUploader(String uploadedBy) {
        return repository.findByUploadedBy(uploadedBy);
    }

    public List<DiscountProof> uploadFiles(String bookingId, String discountType, List<MultipartFile> files) throws IOException {

        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        List<DiscountProof> savedFiles = new ArrayList<>();

        for (MultipartFile file : files) {

            validateFile(file);

            String extension = getFileExtension(file.getOriginalFilename());
            String newFileName = UUID.randomUUID().toString() + extension;

            File destination = new File(folder, newFileName);
            file.transferTo(destination);

            DiscountProof proof = new DiscountProof(
                    bookingId,
                    discountType,
                    newFileName,
                    destination.getAbsolutePath()
            );

            repository.save(proof);
            savedFiles.add(proof);
        }

        return savedFiles;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 5MB.");
        }

        String type = file.getContentType();
        if (!(type.equals("image/jpeg") || type.equals("image/png") || type.equals("image/jpg"))) {
            throw new RuntimeException("Invalid file type. Only JPEG and PNG allowed.");
        }
    }

    private String getFileExtension(String originalName) {
        return originalName.substring(originalName.lastIndexOf("."));
    }
}
