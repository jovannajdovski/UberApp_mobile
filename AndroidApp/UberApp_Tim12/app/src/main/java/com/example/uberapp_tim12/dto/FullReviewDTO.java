package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class FullReviewDTO implements Serializable {

    private ReviewDTO vehicleReview;
    private ReviewDTO driverReview;

    public FullReviewDTO(ReviewDTO vehicleReview, ReviewDTO driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview = driverReview;
    }

    public ReviewDTO getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(ReviewDTO vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public ReviewDTO getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(ReviewDTO driverReview) {
        this.driverReview = driverReview;
    }
}
