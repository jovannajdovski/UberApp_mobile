package com.example.uberapp_tim12.model;

import java.time.LocalDate;

public class DailyRideCount {
    private String day;
    private Integer count;

    public DailyRideCount(String day, Integer count) {
        this.day = day;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
