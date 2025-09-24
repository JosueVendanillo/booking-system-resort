package com.bluebell.project.dto;

public class DashboardStats {
    private double grossRevenue;
    private long totalBookings;
//    private long totalAccounts;
    private long totalCustomers;
//    private long totalAmenities;
//    private long availableRoom

    public DashboardStats(double grossRevenue, long totalBookings, long totalCustomers) {
        this.grossRevenue = grossRevenue;

        this.totalCustomers = totalCustomers;
    }


    public DashboardStats() {
    }

//    private long billCount;


    public double getGrossRevenue() {
        return grossRevenue;
    }


    public void setGrossRevenue(double grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    @Override
    public String toString() {
        return "DashboardStats{" +
                "grossRevenue=" + grossRevenue +
                '}';
    }
}
