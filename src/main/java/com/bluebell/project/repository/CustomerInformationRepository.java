package com.bluebell.project.repository;

import com.bluebell.project.model.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInformationRepository extends JpaRepository<CustomerInformation, Long> {
    // You can add custom queries later if needed

    @Query("SELECT COUNT(*) AS totalCustomers\n" +
            "FROM CustomerInformation")
    Long countTotalCustomers();

    @Query("SELECT ci.fullname FROM CustomerInformation ci WHERE ci.createdBy = :createdBy")
    List<String> findFullNamesByCreatedBy(@Param("createdBy") String createdBy);

}
