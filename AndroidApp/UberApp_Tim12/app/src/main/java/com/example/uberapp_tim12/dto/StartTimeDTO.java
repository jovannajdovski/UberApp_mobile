package com.example.uberapp_tim12.dto;

public class StartTimeDTO {
    String start;

    public StartTimeDTO(String dateTime) {
        this.start = dateTime;
    }

    public String getDateTime() {
        return start;
    }
}
