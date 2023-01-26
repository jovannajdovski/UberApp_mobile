package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageDTO implements Serializable {
    private Integer id;
    private String timeOfSending;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private String type;
    private Integer rideId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(String timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }
}
