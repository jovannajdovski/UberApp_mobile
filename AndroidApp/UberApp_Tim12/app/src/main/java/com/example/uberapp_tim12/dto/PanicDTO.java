package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PanicDTO implements Serializable {

    private Integer id;

    private UserPanicDTO user;

    private RideFullDTO ride;

    private LocalDateTime time;

    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserPanicDTO getUser() {
        return user;
    }

    public void setUser(UserPanicDTO user) {
        this.user = user;
    }

    public RideFullDTO getRide() {
        return ride;
    }

    public void setRide(RideFullDTO ride) {
        this.ride = ride;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
