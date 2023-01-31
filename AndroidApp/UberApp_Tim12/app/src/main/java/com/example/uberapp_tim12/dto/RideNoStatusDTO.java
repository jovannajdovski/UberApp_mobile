package com.example.uberapp_tim12.dto;

import com.example.uberapp_tim12.model.VehicleCategory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RideNoStatusDTO implements Serializable {

    private Integer id;

    private String startTime;

    private String endTime;

    private Double totalCost;

    private UserRideDTO driver;

    private Set<UserRideDTO> passengers = new HashSet<>();

    private Double estimatedTimeInMinutes;

    private VehicleCategory vehicleType;
    private boolean babyTransport;

    private boolean petTransport;

    private RideRejectionDTO rejection;

    private Set<PathDTO> locations = new HashSet<>();

    private String scheduledTime;

    public RideNoStatusDTO(RideFullDTO rideFullDTO) {
        this.id = rideFullDTO.getId();
        this.startTime = rideFullDTO.getStartTime();
        this.endTime = rideFullDTO.getEndTime();
        this.totalCost = rideFullDTO.getTotalCost();
        this.driver = rideFullDTO.getDriver();
        this.passengers = rideFullDTO.getPassengers();
        this.estimatedTimeInMinutes = rideFullDTO.getEstimatedTimeInMinutes();
        this.vehicleType = rideFullDTO.getVehicleType();
        this.babyTransport = rideFullDTO.isBabyTransport();
        this.petTransport = rideFullDTO.isPetTransport();
        this.rejection = rideFullDTO.getRejection();
        this.locations = rideFullDTO.getLocations();
        this.scheduledTime = rideFullDTO.getScheduledTime();
    }

    public RideNoStatusDTO(Integer id, String startTime, String endTime, Double totalCost, UserRideDTO driver, Set<UserRideDTO> passengers, Double estimatedTimeInMinutes, VehicleCategory vehicleType, boolean babyTransport, boolean petTransport, RideRejectionDTO rejection, Set<PathDTO> locations, String scheduledTime) {
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

    public VehicleCategory getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleCategory vehicleType) {
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

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
