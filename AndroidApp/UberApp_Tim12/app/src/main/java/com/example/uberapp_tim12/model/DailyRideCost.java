package com.example.uberapp_tim12.model;

public class DailyRideCost {
    private String day;
    private Integer amount;

    public DailyRideCost(String day, Integer amount) {
        this.day = day;
        this.amount = amount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
