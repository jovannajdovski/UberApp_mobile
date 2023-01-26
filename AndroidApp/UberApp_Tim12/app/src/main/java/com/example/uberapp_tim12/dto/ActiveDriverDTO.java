package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ActiveDriverDTO implements Serializable{
    private VehicleBasicDTO vehicle;
    private LocationDTO location;
    private boolean free;

    public VehicleBasicDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleBasicDTO vehicle) {
        this.vehicle = vehicle;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
