package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class SendingMessageDTO implements Parcelable {

    private String message;
    private String type;
    private Integer rideId;

    public SendingMessageDTO(String message, Integer rideId) {
        this.message = message;
        this.type="RIDE";
        this.rideId = rideId;
    }

    public SendingMessageDTO(String message, String type, Integer rideId) {
        this.message = message;
        this.type = type;
        this.rideId = rideId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    protected SendingMessageDTO(Parcel in) {
        message = in.readString();
        type = in.readString();
        rideId=in.readInt();
    }

    public static final Creator<SendingMessageDTO> CREATOR = new Creator<SendingMessageDTO>() {
        @Override
        public SendingMessageDTO createFromParcel(Parcel in) {
            return new SendingMessageDTO(in);
        }

        @Override
        public SendingMessageDTO[] newArray(int size) {
            return new SendingMessageDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(type);
        dest.writeInt(rideId);
    }
}
