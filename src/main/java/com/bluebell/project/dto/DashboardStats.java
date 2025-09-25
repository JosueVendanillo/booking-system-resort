package com.bluebell.project.dto;

public class DashboardStats {
    private double grossRevenue;
    private long totalBookings;
    private long totalAccounts;
    private long totalCustomers;
    private long totalAmenities;

    private long totalBills;
    private long totalRooms;
    private long availableRoom;


    public DashboardStats(double grossRevenue, long totalBookings, long totalCustomers,
                          long totalAccounts,
                          long totalAmenities, long totalBills, long totalRooms, long availableRoom) {
        this.grossRevenue = grossRevenue;
        this.totalCustomers = totalCustomers;
        this.totalAccounts = totalAccounts;
        this.totalAmenities = totalAmenities;
        this.totalBills = totalBills;
        this.totalRooms = totalRooms;
        this.availableRoom = availableRoom;
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


    public long getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(long totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public long getTotalAmenities() {
        return totalAmenities;
    }

    public void setTotalAmenities(long totalAmenities) {
        this.totalAmenities = totalAmenities;
    }

    public long getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(long totalBills) {
        this.totalBills = totalBills;
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getAvailableRoom() {
        return availableRoom;
    }

    public void setAvailableRoom(long availableRoom) {
        this.availableRoom = availableRoom;
    }

    @Override
    public String toString() {
        return "DashboardStats{" +
                "grossRevenue=" + grossRevenue +
                '}';
    }
}
