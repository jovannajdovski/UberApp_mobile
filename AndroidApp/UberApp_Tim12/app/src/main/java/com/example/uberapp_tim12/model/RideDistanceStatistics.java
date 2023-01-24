package com.example.uberapp_tim12.model;

import java.util.List;

public class RideDistanceStatistics {
    private List<DailyRideDistance> distancePerDay;
    private Integer totalDistance;
    private Integer averageDistance;

    public RideDistanceStatistics(List<DailyRideDistance> distancePerDay, Integer totalDistance, Integer averageDistance) {
        this.distancePerDay = distancePerDay;
        this.totalDistance = totalDistance;
        this.averageDistance = averageDistance;
    }

    public List<DailyRideDistance> getDistancePerDay() {
        return distancePerDay;
    }

    public void setDistancePerDay(List<DailyRideDistance> distancePerDay) {
        this.distancePerDay = distancePerDay;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(Integer averageDistance) {
        this.averageDistance = averageDistance;
    }
}
