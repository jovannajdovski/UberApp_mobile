package com.example.uberapp_tim12.dto;

import android.app.Service;

import java.io.Serializable;

public class UserEmailDTO implements Serializable {
    private String email;

    public UserEmailDTO(String email) {
        this.email = email;
    }
}
