package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.util.List;

public class FullReviewList implements Serializable {

    private List<FullReviewDTO> reviews;

    public FullReviewList(List<FullReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public List<FullReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<FullReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
