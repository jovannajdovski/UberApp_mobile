package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RideRejectionDTO implements Serializable {

    private String reason;

    private LocalDateTime timeOfRejection;

}
