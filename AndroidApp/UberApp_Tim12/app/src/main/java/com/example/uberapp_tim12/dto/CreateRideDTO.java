package com.example.uberapp_tim12.dto;

import com.example.uberapp_tim12.model.VehicleCategory;
import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CreateRideDTO implements Serializable {


    private Set<PathDTO> locations = new HashSet<>();
    private Set<UserRideDTO> passengers = new HashSet<>();
    private VehicleCategory vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    private String scheduledTime;

    public Set<PathDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<PathDTO> locations) {
        this.locations = locations;
    }

    public Set<UserRideDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<UserRideDTO> passengers) {
        this.passengers = passengers;
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

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
