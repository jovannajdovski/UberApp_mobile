package com.example.uberapp_tim12.model;

import com.example.uberapp_tim12.dto.PathDTO;
import com.example.uberapp_tim12.dto.UserEmailDTO;

import java.util.List;

public class FavouriteRide {
    private Integer id;
    private String favoriteName;
    private List<PathDTO> locations;
    private List<UserEmailDTO> passengers;
    private VehicleCategory vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavouriteRide(Integer id, String favoriteName, List<PathDTO> locations, List<UserEmailDTO> passengers, VehicleCategory vehicleType, boolean babyTransport, boolean petTransport) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public List<PathDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<PathDTO> locations) {
        this.locations = locations;
    }

    public List<UserEmailDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserEmailDTO> passengers) {
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
