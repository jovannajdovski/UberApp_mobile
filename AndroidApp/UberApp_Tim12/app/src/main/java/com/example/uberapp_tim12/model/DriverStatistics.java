package com.example.uberapp_tim12.model;

public class DriverStatistics {
    private Integer numberOfRejectedRides;
    private Integer numberOfAcceptedRides;
    private Integer totalWorkHours;
    private Integer totalIncome;

    public DriverStatistics(Integer numberOfRejectedRides, Integer numberOfAcceptedRides, Integer totalWorkHours, Integer totalIncome) {
        this.numberOfRejectedRides = numberOfRejectedRides;
        this.numberOfAcceptedRides = numberOfAcceptedRides;
        this.totalWorkHours = totalWorkHours;
        this.totalIncome = totalIncome;
    }

    public Integer getNumberOfRejectedRides() {
        return numberOfRejectedRides;
    }

    public void setNumberOfRejectedRides(Integer numberOfRejectedRides) {
        this.numberOfRejectedRides = numberOfRejectedRides;
    }

    public Integer getNumberOfAcceptedRides() {
        return numberOfAcceptedRides;
    }

    public void setNumberOfAcceptedRides(Integer numberOfAcceptedRides) {
        this.numberOfAcceptedRides = numberOfAcceptedRides;
    }

    public Integer getTotalWorkHours() {
        return totalWorkHours;
    }

    public void setTotalWorkHours(Integer totalWorkHours) {
        this.totalWorkHours = totalWorkHours;
    }

    public Integer getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Integer totalIncome) {
        this.totalIncome = totalIncome;
    }
}
