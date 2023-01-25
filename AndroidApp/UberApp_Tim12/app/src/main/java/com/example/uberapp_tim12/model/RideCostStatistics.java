package com.example.uberapp_tim12.model;

import java.util.List;

public class RideCostStatistics {
    private List<DailyRideCost> amountPerDay;
    private Integer totalAmount;
    private Integer averageAmount;

    public RideCostStatistics(List<DailyRideCost> amountPerDay, Integer totalAmount, Integer averageAmount) {
        this.amountPerDay = amountPerDay;
        this.totalAmount = totalAmount;
        this.averageAmount = averageAmount;
    }

    public List<DailyRideCost> getAmountPerDay() {
        return amountPerDay;
    }

    public void setAmountPerDay(List<DailyRideCost> amountPerDay) {
        this.amountPerDay = amountPerDay;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(Integer averageAmount) {
        this.averageAmount = averageAmount;
    }
}
