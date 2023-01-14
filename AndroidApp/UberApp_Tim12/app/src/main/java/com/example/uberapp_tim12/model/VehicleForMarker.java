package com.example.uberapp_tim12.model;

public class VehicleForMarker {
    private Integer id;
    private Integer driverId;
    private String vehicleType;
    private String model;
    private String licenseNumber;
    private Integer passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;
    private boolean active;
    private Double latitude;
    private Double longitude;
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public VehicleForMarker(Integer id, Integer driverId, String vehicleType, String model, String licenseNumber, Integer passengerSeats, boolean babyTransport, boolean petTransport, boolean active, Double latitude, Double longitude, String address) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.passengerSeats = passengerSeats;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.active = active;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public VehicleForMarker() {
    }

    @Override
    public String toString() {
        return "VehicleForMarker{" +
                "id=" + id +
                ", driverId=" + driverId +
                ", vehicleType='" + vehicleType + '\'' +
                ", model='" + model + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", passengerSeats=" + passengerSeats +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", active=" + active +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setPassengerSeats(Integer passengerSeats) {
        this.passengerSeats = passengerSeats;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getModel() {
        return model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public Integer getPassengerSeats() {
        return passengerSeats;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public boolean isActive() {
        return active;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
