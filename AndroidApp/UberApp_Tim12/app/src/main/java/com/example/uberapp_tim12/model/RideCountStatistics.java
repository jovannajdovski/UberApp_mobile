package com.example.uberapp_tim12.model;

import java.util.List;

public class RideCountStatistics {
    private List<DailyRideCount> countPerDay;
    private Integer totalCount;
    private Double averageCount;

    public RideCountStatistics(List<DailyRideCount> countPerDay, Integer totalCount, Double averageCount) {
        this.countPerDay = countPerDay;
        this.totalCount = totalCount;
        this.averageCount = averageCount;
    }

    public List<DailyRideCount> getCountPerDay() {
        return countPerDay;
    }

    public void setCountPerDay(List<DailyRideCount> countPerDay) {
        this.countPerDay = countPerDay;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(Double averageCount) {
        this.averageCount = averageCount;
    }
}
