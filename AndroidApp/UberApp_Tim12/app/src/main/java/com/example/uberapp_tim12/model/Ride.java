package com.example.uberapp_tim12.model;

import android.os.Parcelable;

import com.example.uberapp_tim12.dto.PathDTO;
import com.example.uberapp_tim12.dto.RideRejectionDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Ride implements Serializable{
    private Integer id;

    private String startTime;

    private String endTime;
//    private LocalDateTime startTime;

//    private LocalDateTime endTime;

    private Double totalCost;

    //private String driver;
    private UserRideDTO driver;

    //private Set<String> passengers=new HashSet<>();
    private Set<UserRideDTO> passengers = new HashSet<>();

    private Double estimatedTimeInMinutes;

    private String vehicleType;
    //private VehicleCategory vehicleType;

    private boolean babyTransport;

    private boolean petTransport;

    private RideRejectionDTO rejection;

    private Set<PathDTO> locations = new HashSet<>();

    private String status;
//    private RideStatus status;

    private String scheduledTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public UserRideDTO getDriver() {
        return driver;
    }

    public void setDriver(UserRideDTO driver) {
        this.driver = driver;
    }

    public Set<UserRideDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<UserRideDTO> passengers) {
        this.passengers = passengers;
    }

    public Double getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Double estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public RideRejectionDTO getRejection() {
        return rejection;
    }

    public void setRejection(RideRejectionDTO rejection) {
        this.rejection = rejection;
    }

    public Set<PathDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<PathDTO> locations) {
        this.locations = locations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
