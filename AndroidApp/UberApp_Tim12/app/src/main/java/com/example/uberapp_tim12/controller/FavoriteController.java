package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.CreateFavoriteDTO;
import com.example.uberapp_tim12.dto.FavoriteFullDTO;
import com.example.uberapp_tim12.dto.FavoriteRouteForPassengerDTO;
import com.example.uberapp_tim12.dto.PassengerRegistrationDTO;
import com.example.uberapp_tim12.dto.RidePageList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteController {
    @GET("ride/favorites/passenger/ride")
    Call<FavoriteRouteForPassengerDTO> isFavorite(@Query("from") String from, @Query("to") String to, @Header("Authorization") String token);

    @POST("ride/favorites")
    Call<FavoriteFullDTO> createFavorite(@Body CreateFavoriteDTO createFavoriteDTO, @Header("Authorization") String token);

    @DELETE("ride/favorites/{id}")
    Call<ResponseBody> deleteFavorite(@Path("id") Integer id, @Header("Authorization") String token);
}
