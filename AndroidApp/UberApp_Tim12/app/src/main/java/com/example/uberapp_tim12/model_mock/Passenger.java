package com.example.uberapp_tim12.model_mock;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Passenger implements Serializable {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;

    public Passenger() {
    }

    public Passenger(String name, String surname, String email, String phoneNumber, String address, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "name: " + name + "\n"  +
                "surname: " + surname + "\n" +
                "email: " + email + "\n" +
                "phoneNumber: " + phoneNumber + "\n" +
                "address: " + address + "\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
