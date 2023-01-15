package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class ReasonDTO implements Serializable {

    String reason;

    public ReasonDTO(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
