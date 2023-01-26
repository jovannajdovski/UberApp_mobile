package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveDriverListDTO implements Parcelable {
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    @SerializedName("results")
    @Expose
    private List<ActiveDriverDTO> drivers;

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

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ActiveDriverDTO> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<ActiveDriverDTO> drivers) {
        this.drivers = drivers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeList(drivers);
    }
}
