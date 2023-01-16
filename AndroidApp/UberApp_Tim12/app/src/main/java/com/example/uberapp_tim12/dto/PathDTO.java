package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class PathDTO implements Serializable {
    private LocationDTO departure;

    private LocationDTO destination;

    public LocationDTO getDeparture() {
        return departure;
    }

    public void setDeparture(LocationDTO departure) {
        this.departure = departure;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(LocationDTO destination) {
        this.destination = destination;
    }

    public PathDTO(LocationDTO departure, LocationDTO destination) {
        this.departure = departure;
        this.destination = destination;
    }
}
