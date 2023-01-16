package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class LoginUserDTO implements Serializable {
    private String email;
    private String password;

    public LoginUserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
