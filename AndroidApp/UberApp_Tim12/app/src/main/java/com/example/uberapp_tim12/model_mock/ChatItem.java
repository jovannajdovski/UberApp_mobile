package com.example.uberapp_tim12.model_mock;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatItem implements Parcelable {
    private String route;
    private String dateTime;
    private int icon;
    private MessageType messageType;
    public enum MessageType {
        DRIVER,
        SUPPORT,
        PANIC
    }
    public ChatItem(String route, String dateTime, int icon, MessageType messageType)
    {
        this.route=route;
        this.dateTime=dateTime;
        this.icon=icon;
        this.messageType=messageType;
    }
    protected ChatItem(Parcel in) {
        route=in.readString();
        dateTime=in.readString();
        icon=in.readInt();
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
    public int getColor() {
        switch (this.messageType)
        {
            case DRIVER:
                return 0x1800FF00;
            case PANIC:
                return 0x18FF0000;
            case SUPPORT:
                return 0x18FFFF00;
            default:
                return 0xFFFFFFFF;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(route);
        dest.writeString(dateTime);
        dest.writeInt(icon);
    }


}
