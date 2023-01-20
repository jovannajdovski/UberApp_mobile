package com.example.uberapp_tim12.model_mock;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.uberapp_tim12.dto.MessageDTO;

import java.io.Serializable;
import java.util.List;

public class ChatItem implements Serializable {
    private String route;
    private String dateTime;
    private int icon;
    private int rideId;
    private List<MessageDTO> messages;

    public ChatItem(String route, String dateTime, int icon, int rideId, List<MessageDTO> messagesDTO)
    {
        this.route=route;
        this.dateTime=dateTime;
        this.icon=icon;
        this.rideId=rideId;
        this.messages=messagesDTO;
    }



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
}
