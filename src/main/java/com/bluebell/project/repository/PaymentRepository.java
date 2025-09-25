package com.bluebell.project.repository;

import com.bluebell.project.dto.MonthlyRevenue;
import com.bluebell.project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBooking_Id(Long bookingId);
    List<Payment> findByBooking_BookingCode(String bookingCode);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status")
    double sumByStatus(@Param("status") String status);

    @Query("SELECT FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m') AS month, SUM(p.amount) AS total " +
            "FROM Payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m')")
    List<Object[]> sumByMonth();

    @Query("SELECT COUNT(*) AS totalBills\n" +
            "FROM Payment")
    Long countTotalBills();

}
