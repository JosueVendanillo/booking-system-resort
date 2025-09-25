package com.bluebell.project.dto;


public class MonthlyRevenue {
    private Object month; // format: yyyy-MM
    private double total;

    public MonthlyRevenue(Object month, double total) {
        this.month = month;
        this.total = total;
    }

    public Object getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
