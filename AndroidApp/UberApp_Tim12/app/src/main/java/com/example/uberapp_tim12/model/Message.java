package com.example.uberapp_tim12.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Serializable {

    public enum Sender {
        MYSELF(0), OTHERS(1);
        private final int value;
        private Sender(int value)
        {
            this.value=value;
        }
        private int getValue()
        {
            return value;
        }
    }
    String text;
    Sender sender;

    public Message(String text, Sender sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public Sender getSender() {
        return sender;
    }
}
