package com.example.uberapp_tim12.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RideFullDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("startTime")
    @Expose
    private String startTime;

    @SerializedName("endTime")
    @Expose
    private String endTime;

    @SerializedName("totalCost")
    @Expose
    private Double totalCost;

    @SerializedName("driver")
    @Expose
    private UserRideDTO driver;

    @SerializedName("passengers")
    @Expose
    private Set<UserRideDTO> passengers = new HashSet<>();

    @SerializedName("estimatedTimeInMinutes")
    @Expose
    private Double estimatedTimeInMinutes;

    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

    @SerializedName("babyTransport")
    @Expose
    private boolean babyTransport;

    @SerializedName("petTransport")
    @Expose
    private boolean petTransport;

    @SerializedName("rejection")
    @Expose
    private RideRejectionDTO rejection;

    @SerializedName("locations")
    @Expose
    private Set<PathDTO> locations = new HashSet<>();

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("scheduledTime")
    @Expose
    private String scheduledTime;

    public RideFullDTO(Integer id, String startTime, String endTime, Double totalCost, UserRideDTO driver, Set<UserRideDTO> passengers, Double estimatedTimeInMinutes, String vehicleType, boolean babyTransport, boolean petTransport, RideRejectionDTO rejection, Set<PathDTO> locations, String status, String scheduledTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
        this.passengers = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.rejection = rejection;
        this.locations = locations;
        this.status = status;
        this.scheduledTime = scheduledTime;
    }

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
