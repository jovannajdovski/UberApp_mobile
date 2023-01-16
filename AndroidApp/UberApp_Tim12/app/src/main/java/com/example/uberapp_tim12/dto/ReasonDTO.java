package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ReasonDTO implements Parcelable {
    String reason;

    public ReasonDTO(String reason) {
        this.reason = reason;
    }

    protected ReasonDTO(Parcel in) {
        reason = in.readString();
    }

    public static final Creator<ReasonDTO> CREATOR = new Creator<ReasonDTO>() {
        @Override
        public ReasonDTO createFromParcel(Parcel in) {
            return new ReasonDTO(in);
        }

        @Override
        public ReasonDTO[] newArray(int size) {
            return new ReasonDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reason);
    }
}
