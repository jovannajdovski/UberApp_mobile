package com.example.uberapp_tim12.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NavDrawerItem implements Parcelable{

    private String title;
    private int icon;

    public NavDrawerItem(String title, int icon)
    {
        this.title=title;
        this.icon=icon;
    }
    protected NavDrawerItem(Parcel in) {
        title=in.readString();
        icon=in.readInt();
    }
    public String getTitle(){return this.title;}
    public int getIcon(){return this.icon;}
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(icon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NavDrawerItem> CREATOR = new Creator<NavDrawerItem>() {
        @Override
        public NavDrawerItem createFromParcel(Parcel in) {
            return new NavDrawerItem(in);
        }

        @Override
        public NavDrawerItem[] newArray(int size) {
            return new NavDrawerItem[size];
        }
    };

    @Override
    public String toString() {

        return title;
    }
}
