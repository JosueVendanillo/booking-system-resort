package com.bluebell.project.repository;

import com.bluebell.project.model.DiscountProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountProofRepository extends JpaRepository<DiscountProof, Long> {

    List<DiscountProof> findByUploadedBy(String uploadedBy);
}
