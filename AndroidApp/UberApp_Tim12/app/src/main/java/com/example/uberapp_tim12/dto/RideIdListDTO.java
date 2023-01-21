package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.util.List;

public class RideIdListDTO implements Serializable {
    List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public RideIdListDTO(List<Integer> ids) {
        this.ids = ids;
    }
}
