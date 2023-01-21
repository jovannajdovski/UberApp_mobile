package com.example.uberapp_tim12.dto;

import com.example.uberapp_tim12.model.Ride;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RidePageList implements Serializable {

    private Integer totalCount;

    private List<RideNoStatusDTO> results;

    public RidePageList(Integer totalCount, List<RideNoStatusDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<RideNoStatusDTO> getResults() {
        return results;
    }

    public void setResults(List<RideNoStatusDTO> results) {
        this.results = results;
    }
}
