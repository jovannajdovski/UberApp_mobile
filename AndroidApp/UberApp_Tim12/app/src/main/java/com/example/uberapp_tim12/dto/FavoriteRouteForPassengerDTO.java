package com.example.uberapp_tim12.dto;

import java.io.Serializable;

public class FavoriteRouteForPassengerDTO implements Serializable {

    private boolean isFavorite;
    private Integer favoriteId;

    public FavoriteRouteForPassengerDTO(boolean isFavorite, Integer favoriteId) {
        this.isFavorite = isFavorite;
        this.favoriteId = favoriteId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Integer favoriteId) {
        this.favoriteId = favoriteId;
    }
}
