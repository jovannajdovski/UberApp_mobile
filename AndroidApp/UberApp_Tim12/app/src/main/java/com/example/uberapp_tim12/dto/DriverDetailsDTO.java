package com.example.uberapp_tim12.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDetailsDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;

    @SerializedName("telephoneNumber")
    @Expose
    private String telephoneNumber;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("address")
    @Expose
    private String address;

}
