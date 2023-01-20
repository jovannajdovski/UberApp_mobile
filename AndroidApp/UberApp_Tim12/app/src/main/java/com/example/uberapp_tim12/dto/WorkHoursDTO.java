package com.example.uberapp_tim12.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class WorkHoursDTO implements Parcelable {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;

    public WorkHoursDTO(Integer id, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeSerializable(start);
        dest.writeSerializable(end);
    }
}
