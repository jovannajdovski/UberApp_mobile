package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PassengerDTO implements Parcelable {
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    protected PassengerDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        surname = in.readString();
        profilePicture = in.readString();
        telephoneNumber = in.readString();
        email = in.readString();
        address = in.readString();
    }

    public static final Creator<PassengerDTO> CREATOR = new Creator<PassengerDTO>() {
        @Override
        public PassengerDTO createFromParcel(Parcel in) {
            return new PassengerDTO(in);
        }

        @Override
        public PassengerDTO[] newArray(int size) {
            return new PassengerDTO[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.profilePicture);
        dest.writeString(this.telephoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.address);
    }
}
