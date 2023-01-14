package com.example.uberapp_tim12.model;

import java.util.List;

public class FavouriteRide {
    String departure;
    String destination;
    String name;
    List<String> passengers;

    public FavouriteRide(String departure, String destination, String name, List<String> passengers) {
        this.departure = departure;
        this.destination = destination;
        this.name = name;
        this.passengers = passengers;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }
}
