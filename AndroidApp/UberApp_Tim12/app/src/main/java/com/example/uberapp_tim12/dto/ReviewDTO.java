package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class ReviewDTO implements Serializable {

    private Integer id;

    private Integer rating;

    private String comment;

    private UserRideDTO passenger;

    public ReviewDTO(Integer id, Integer rating, String comment, UserRideDTO passenger) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserRideDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(UserRideDTO passenger) {
        this.passenger = passenger;
    }
}
