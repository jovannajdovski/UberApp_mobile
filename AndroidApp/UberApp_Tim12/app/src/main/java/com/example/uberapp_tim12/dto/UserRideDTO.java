package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class UserRideDTO implements Serializable {
    private Integer id;
    private String email;

    public UserRideDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
    @Override
    public String toString() {
        return email;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
