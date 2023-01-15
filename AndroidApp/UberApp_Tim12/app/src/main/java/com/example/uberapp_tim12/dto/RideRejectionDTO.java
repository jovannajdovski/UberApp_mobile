package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RideRejectionDTO implements Serializable {

    private String reason;

    private String timeOfRejection;

    public RideRejectionDTO(String reason, String timeOfRejection) {
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(String timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }
}
