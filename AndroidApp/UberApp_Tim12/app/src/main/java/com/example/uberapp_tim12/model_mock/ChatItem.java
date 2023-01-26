package com.example.uberapp_tim12.model_mock;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.uberapp_tim12.dto.MessageDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatItem implements Parcelable {
    private String route;
    private String dateTime;
    private int icon;
    private int rideId;
    private int otherPersonId;
    private List<MessageDTO> messages;

    public ChatItem(String route, String dateTime, int icon, int rideId, List<MessageDTO> messagesDTO, int otherPersonId)
    {
        this.route=route;
        this.dateTime=dateTime;
        this.icon=icon;
        this.rideId=rideId;
        this.messages=messagesDTO;
        this.otherPersonId=otherPersonId;
    }

    public ChatItem(int icon, int rideId, List<MessageDTO> messages, int otherPersonId) {
        this.icon = icon;
        this.rideId = rideId;
        this.messages = messages;
        this.otherPersonId=otherPersonId;
    }


    protected ChatItem(Parcel in) {
        route = in.readString();
        dateTime = in.readString();
        icon = in.readInt();
        rideId = in.readInt();
        otherPersonId=in.readInt();
        messages = new ArrayList<MessageDTO>();
        in.readList(messages, MessageDTO.class.getClassLoader());
    }

    public static final Creator<ChatItem> CREATOR = new Creator<ChatItem>() {
        @Override
        public ChatItem createFromParcel(Parcel in) {
            return new ChatItem(in);
        }

        @Override
        public ChatItem[] newArray(int size) {
            return new ChatItem[size];
        }
    };

    public String getRoute(){return this.route;}
    public String getDateTime(){return this.dateTime;}
    public int getIcon(){return this.icon;}

    public void setRoute(String route) {
        this.route = route;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public int getOtherPersonId() {
        return otherPersonId;
    }

    public void setOtherPersonId(int otherPersonId) {
        this.otherPersonId = otherPersonId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.route);
        dest.writeString(this.dateTime);
        dest.writeInt(this.icon);
        dest.writeInt(this.rideId);
        dest.writeInt(this.otherPersonId);
        dest.writeList(this.messages);
    }
}
