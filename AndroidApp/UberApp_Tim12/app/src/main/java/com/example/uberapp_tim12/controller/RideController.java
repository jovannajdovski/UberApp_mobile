package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.dto.RideIdListDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.model.FavouriteRide;
import com.example.uberapp_tim12.model.Ride;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RideController {
    @GET("driver/{id}/ride/pending")
    Call<RidesListDTO> getPendingRidesForDriver(@Path("id") Integer id, @Header("Authorization") String token);

    @PUT("ride/{id}/accept")
    Call<Ride> acceptRide(@Path("id") Integer rideId, @Header("Authorization") String token);

    @PUT("ride/{id}/start")
    Call<Ride> startRide(@Path("id") Integer rideId, @Header("Authorization") String token);

    @PUT("ride/{id}/cancel")
    Call<Ride> rejectRide(@Path("id") Integer rideId, @Body ReasonDTO reasonDTO, @Header("Authorization") String token);

    @POST("ride")
    Call<RideFullDTO> createRide(@Body CreateRideDTO createRideDTO, @Header("Authorization") String jwt);

    @GET("ride/driver/{driverId}/active")
    Call<RideFullDTO> getActiveRideForDriver(@Path("driverId") Integer driverId, @Header("Authorization") String token);

    @GET("ride/driver/{driverId}/accepted")
    Call<RidePageList> getAcceptedRidesForDriver(@Path("driverId") Integer driverId, @Header("Authorization") String token);

    @GET("ride/passenger/{passengerId}/active")
    Call<RideFullDTO> getActiveRideForPassenger(@Path("passengerId") Integer passengerId, @Header("Authorization") String token);

    @GET("ride/favorites/{passengerId}")
    Call<List<FavouriteRide>> getFavoritesForPassenger(@Path("passengerId") Integer passengerId, @Header("Authorization") String token);

    @DELETE("ride/favorites/{passengerId}")
    Call<String> deleteFavoriteForPassenger(@Path("passengerId") Integer passengerId, @Header("Authorization") String token);
    
    @PUT("ride/specific-rides")
    Call<RidesListDTO> getRidesDetailsFromIdList(@Body RideIdListDTO rideIdListDTO, @Header("Authorization") String token);

}
