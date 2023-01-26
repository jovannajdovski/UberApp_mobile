package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class WorkHoursDTO implements Parcelable {
    private Integer id;
    private String start;
    private String end;

    public WorkHoursDTO(Integer id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }
    public WorkHoursDTO()
    {

    }

    protected WorkHoursDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
    }

    public static final Creator<WorkHoursDTO> CREATOR = new Creator<WorkHoursDTO>() {
        @Override
        public WorkHoursDTO createFromParcel(Parcel in) {
            return new WorkHoursDTO(in);
        }

        @Override
        public WorkHoursDTO[] newArray(int size) {
            return new WorkHoursDTO[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(start);
        dest.writeString(end);
    }
}
