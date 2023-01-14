package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.uberapp_tim12.model.Ride;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RidesListDTO implements Parcelable {
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    @SerializedName("results")
    @Expose
    private List<Ride> rides;

    protected RidesListDTO(Parcel in) {
        if (in.readByte() == 0) {
            totalCount = null;
        } else {
            totalCount = in.readInt();
        }
    }

    public static final Creator<RidesListDTO> CREATOR = new Creator<RidesListDTO>() {
        @Override
        public RidesListDTO createFromParcel(Parcel in) {
            return new RidesListDTO(in);
        }

        @Override
        public RidesListDTO[] newArray(int size) {
            return new RidesListDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeList(rides);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
