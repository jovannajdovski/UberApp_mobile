package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class ReviewRequestDTO implements Serializable {
    private Double rating;
    private String comment;

    public ReviewRequestDTO(Double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
