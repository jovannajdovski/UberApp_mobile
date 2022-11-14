package com.example.uberapp_tim12.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationItem implements Parcelable {
    private String route;
    private String dateTime;
    private NotificationType notificationType;
    private String information;
    private String reason;
    private String notificationDateTime;
    public enum NotificationType {
        REMINDER,
        CANCELLATION
    }

    public NotificationItem(String route, String dateTime, NotificationType notificationType, String information, String reason, String notificationDateTime) {
        this.route = route;
        this.dateTime = dateTime;
        this.notificationType = notificationType;
        this.information = information;
        this.reason = reason;
        this.notificationDateTime=notificationDateTime;
    }

    protected NotificationItem(Parcel in) {
        route = in.readString();
        dateTime = in.readString();
        information = in.readString();
        reason = in.readString();
        notificationDateTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(route);
        dest.writeString(dateTime);
        dest.writeString(information);
        dest.writeString(reason);
        dest.writeString(notificationDateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationItem> CREATOR = new Creator<NotificationItem>() {
        @Override
        public NotificationItem createFromParcel(Parcel in) {
            return new NotificationItem(in);
        }

        @Override
        public NotificationItem[] newArray(int size) {
            return new NotificationItem[size];
        }
    };

    public String getRoute() {
        return route;
    }

    public String getDateTime() {
        return dateTime;
    }


    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getInformation() {
        return information;
    }

    public String getReason() {
        return reason;
    }

    public String getNotificationDateTime() {
        return notificationDateTime;
    }
}
