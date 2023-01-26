package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageListDTO implements Parcelable {
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    @SerializedName("results")
    @Expose
    private List<MessageDTO> messages;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    protected MessageListDTO(Parcel in) {
        if (in.readByte() == 0) {
            totalCount = null;
        } else {
            totalCount = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeList(messages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageListDTO> CREATOR = new Creator<MessageListDTO>() {
        @Override
        public MessageListDTO createFromParcel(Parcel in) {
            return new MessageListDTO(in);
        }

        @Override
        public MessageListDTO[] newArray(int size) {
            return new MessageListDTO[size];
        }
    };
}
