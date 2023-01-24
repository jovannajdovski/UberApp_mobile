package com.example.uberapp_tim12.model;

public class DailyRideDistance {
    private String day;
    private Integer distance;

    public DailyRideDistance(String day, Integer distance) {
        this.day = day;
        this.distance = distance;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
