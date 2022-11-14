package com.example.uberapp_tim12.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ride implements Serializable {
    private List<Passenger> passengers;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String messages;
    private String startPlace;
    private String endPlace;
    private double distance;
    private int price;
    private double rate;
    private Passenger driver;

    public Ride(String startDate, String startTime, String endDate, String endTime, String messages, double distance, int price, String startPlace, String endPlace) {
        this.passengers = new ArrayList<Passenger>();
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.messages = messages;
        this.distance = distance;
        this.price = price;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.rate = -1;
    }

    public Ride(String startDate, String startTime, String endDate, String endTime, String messages, double distance, int price, String startPlace, String endPlace, double rate) {
        this.passengers = new ArrayList<Passenger>();
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.messages = messages;
        this.distance = distance;
        this.price = price;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.rate = rate;
    }

    public Passenger getDriver() {
        return driver;
    }

    public void setDriver(Passenger driver) {
        this.driver = driver;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public List<Passenger> getPassangers() {
        return passengers;
    }

    public void setPassangers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void addPassanger(Passenger passanger) {
        this.passengers.add(passanger);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
