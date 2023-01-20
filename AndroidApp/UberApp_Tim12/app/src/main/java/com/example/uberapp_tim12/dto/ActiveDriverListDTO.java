package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.uberapp_tim12.model.Ride;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveDriverListDTO implements Parcelable {

    private Integer totalCount;

    private List<ActiveDriverDTO> rides;

    protected ActiveDriverListDTO(Parcel in) {
        if (in.readByte() == 0) {
            totalCount = null;
        } else {
            totalCount = in.readInt();
        }
    }

    public static final Creator<ActiveDriverListDTO> CREATOR = new Creator<ActiveDriverListDTO>() {
        @Override
        public ActiveDriverListDTO createFromParcel(Parcel in) {
            return new ActiveDriverListDTO(in);
        }

        @Override
        public ActiveDriverListDTO[] newArray(int size) {
            return new ActiveDriverListDTO[size];
        }
    };

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<ActiveDriverDTO> getRides() {
        return rides;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeList(rides);
    }
}
