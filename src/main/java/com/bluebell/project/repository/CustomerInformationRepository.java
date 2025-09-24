package com.bluebell.project.repository;

import com.bluebell.project.model.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInformationRepository extends JpaRepository<CustomerInformation, Long> {
    // You can add custom queries later if needed

    @Query("SELECT COUNT(*) AS totalCustomers\n" +
            "FROM CustomerInformation")
    Long countTotalCustomers();
}
