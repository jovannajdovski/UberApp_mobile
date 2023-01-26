package com.example.uberapp_tim12.dto;

import java.io.Serializable;
import java.util.List;

public class ReviewsForRideDTO implements Serializable {

    private Integer rideId;
    private List<FullReviewDTO> reviews;

    public ReviewsForRideDTO(Integer rideId, List<FullReviewDTO> reviews) {
        this.rideId = rideId;
        this.reviews = reviews;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public List<FullReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<FullReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
