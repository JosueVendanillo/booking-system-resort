package com.bluebell.project.service;

import com.bluebell.project.dto.DashboardStats;
import com.bluebell.project.repository.BookingRepository;
import com.bluebell.project.repository.CustomerInformationRepository;
import com.bluebell.project.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final PaymentRepository paymentRepository;
    private  final BookingRepository bookingRepository;

    private final CustomerInformationRepository customerInformationRepository;

    public DashboardService(PaymentRepository paymentRepository, BookingRepository bookingRepository, CustomerInformationRepository customerInformationRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.customerInformationRepository = customerInformationRepository;
    }
//    private final BookingRepository bookingRepository;
//    private final UserRepository userRepository;
//    private final CustomerRepository customerRepository;

//    public DashboardService(PaymentRepository paymentRepository,
//                            BookingRepository bookingRepository,
//                            UserRepository userRepository,
//                            CustomerRepository customerRepository) {
//        this.paymentRepository = paymentRepository;
//        this.bookingRepository = bookingRepository;
//        this.userRepository = userRepository;
//        this.customerRepository = customerRepository;
//    }

    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        //  Gross revenue from completed payments
        double grossRevenue = paymentRepository.sumByStatus("COMPLETED");

        System.out.println("GROSS REVENUE: " + grossRevenue);

        stats.setGrossRevenue(paymentRepository.sumByStatus("COMPLETED"));
        stats.setTotalBookings(bookingRepository.countBookingsThisYear());
        stats.setTotalCustomers(customerInformationRepository.count());

        // Return DTO with correct constructor usage
        return stats;
    }



    public List<Map<String, Object>> getMonthlyRevenue() {
        List<Object[]> results = paymentRepository.sumByMonth();

        List<Map<String, Object>> monthlyRevenue = new ArrayList<>();
        for (Object[] row : results) {
            String month = (String) row[0];     // "2025-09"
            Double total = (Double) row[1];     // revenue

            Map<String, Object> data = new HashMap<>();
            data.put("month", month);
            data.put("total", total);
            monthlyRevenue.add(data);
        }

        return monthlyRevenue;
    }

    public Long getTotalCustomers() {
        return customerInformationRepository.countTotalCustomers();
    }
}
