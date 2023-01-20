package com.example.uberapp_tim12.dto;

import com.example.uberapp_tim12.model.VehicleCategory;

public class VehicleBasicDTO {

    private VehicleCategory vehicleType;

    private String model;

    private String licenseNumber;

    public VehicleCategory getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleCategory vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
