package com.example.uberapp_tim12.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ErrorMessageDTO implements Serializable {
    @SerializedName("message")
    @Expose
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
