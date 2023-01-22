package com.example.uberapp_tim12.dto;

import com.example.uberapp_tim12.model.VehicleCategory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CreateFavoriteDTO implements Serializable {

    private String favoriteName;

    private Set<PathDTO> locations = new HashSet<>();

    private Set<UserRideDTO> passengers = new HashSet<>();

    private VehicleCategory vehicleType;

    private boolean babyTransport;

    private boolean petTransport;

    public CreateFavoriteDTO(String favoriteName, Set<PathDTO> locations, Set<UserRideDTO> passengers, VehicleCategory vehicleType, boolean babyTransport, boolean petTransport) {
        this.favoriteName = favoriteName;
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

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
}
