package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class ChatMessageDTO implements Serializable {

    private String message;
    private Integer fromId;
    private Integer rideId;

    public ChatMessageDTO(String message, Integer fromId, Integer rideId) {
        this.message = message;
        this.fromId = fromId;
        this.rideId = rideId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }
}
