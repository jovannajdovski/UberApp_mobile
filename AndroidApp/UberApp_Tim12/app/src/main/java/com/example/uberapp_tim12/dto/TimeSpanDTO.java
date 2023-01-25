package com.example.uberapp_tim12.dto;

import java.time.LocalDateTime;

public class TimeSpanDTO {
    private LocalDateTime from;
    private LocalDateTime to;

    public TimeSpanDTO(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
