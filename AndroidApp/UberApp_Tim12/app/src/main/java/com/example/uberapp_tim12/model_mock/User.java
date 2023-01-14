package com.example.uberapp_tim12.model_mock;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String surname;
    private String profileImage;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private boolean blocked;

    public User(String name, String surname, String phoneNumber, String email, String address, String password) {
        this.name = name;
        this.surname = surname;
        this.profileImage = "";
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
        this.blocked = false;
    }

    public User(Parcel parcel) {
        this.name = parcel.readString();
        this.surname = parcel.readString();
        this.profileImage = parcel.readString();
        this.phoneNumber = parcel.readString();
        this.email = parcel.readString();
        this.address = parcel.readString();
        this.password = parcel.readString();
        this.blocked = parcel.readBoolean();
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(profileImage);
        parcel.writeString(phoneNumber);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(password);
        parcel.writeBoolean(blocked);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User {" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", blocked=" + blocked +
                '}';
    }
}
