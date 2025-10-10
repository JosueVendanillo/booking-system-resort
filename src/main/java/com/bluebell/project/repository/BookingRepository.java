package com.bluebell.project.repository;

import com.bluebell.project.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings for the same unit that overlap with a given range
    @Query("""
    select b from Booking b
    where b.unitType = :unitType
      and (:excludeId is null or b.id <> :excludeId)
      and :checkIn < b.checkOut
      and :checkOut > b.checkIn
    """)
    List<Booking> findOverlapping(
            @Param("unitType") String unitType,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut,
            @Param("excludeId") Long excludeId
    );

    // optional: find by unit type
    List<Booking> findByUnitTypeOrderByCheckInAsc(String unitType);


    Optional<Booking> findByBookingCode(String bookingCode);


    @Query("SELECT COUNT(b) FROM Booking b WHERE YEAR(b.createdAt) = YEAR(CURRENT_DATE)")
    Long countBookingsThisYear();


}