package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipleSendingMessageDTO implements Parcelable {
    SendingMessageDTO message;
    List<Integer> userIds;

    protected MultipleSendingMessageDTO(Parcel in) {
        message = in.readParcelable(SendingMessageDTO.class.getClassLoader());
        userIds = new ArrayList<Integer>();
        in.readList(userIds, Integer.class.getClassLoader());

    }

    public static final Creator<MultipleSendingMessageDTO> CREATOR = new Creator<MultipleSendingMessageDTO>() {
        @Override
        public MultipleSendingMessageDTO createFromParcel(Parcel in) {
            return new MultipleSendingMessageDTO(in);
        }

        @Override
        public MultipleSendingMessageDTO[] newArray(int size) {
            return new MultipleSendingMessageDTO[size];
        }
    };

    public SendingMessageDTO getMessage() {
        return message;
    }

    public void setMessage(SendingMessageDTO message) {
        this.message = message;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public MultipleSendingMessageDTO(SendingMessageDTO message, List<Integer> userIds) {
        this.message = message;
        this.userIds = userIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(message,flags);
        dest.writeList(userIds);
    }
}
